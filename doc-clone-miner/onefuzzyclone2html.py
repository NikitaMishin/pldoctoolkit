#!/usr/bin/env python3
# -*- coding: utf-8 -*-

# requires https://pypi.python.org/pypi/PyContracts

import argparse
import logging
import os
import re
import shutil

import intervaltree

import sourcemarkers
import util
from xmllexer import IntervalType

__all__ = ['get_variative_elements']

logging.basicConfig(filename='onefuzzyclone2html.log', level=logging.INFO)
logger = logging

def initargs():
    argpar = argparse.ArgumentParser()
    argpar.add_argument("-ms", "--minimal-similarity", type=float, default=0.5, help="Minimal Levenstein similarity")
    argpar.add_argument("-id", "--input-document", help="Document to analyze", required=True)
    argpar.add_argument("-pn", "--pattern", help="Pattern to search", required=False, default=None)
    argpar.add_argument("-od", "--output-directory", help="Report output directory", required=True)
    argpar.add_argument("-oui", "--only-ui",
                        help="Only generate data needed by standalone [Qt] UI", default="yes")
    return argpar.parse_args()


def tokens(text):
    wre = re.compile("\w+", re.UNICODE)
    r = []
    for m in wre.finditer(text):
        s = m.start()
        e = m.end()
        r.append((s, e, text[s:e]))
    return r

def ctokens(text):
    return ' '.join(tokens(text))

def find_like_pattern(inputfile, pattern, ms):
    # Support ignored/accepted ranges
    marked = sourcemarkers.find_marked_intervals(inputfile.text)
    marked_tree = intervaltree.IntervalTree([
        intervaltree.Interval(b, e)  # include e here to simulate closed interval
        for b, e, t in marked
    ])

    # Tokenize both document and pattern
    textintervals = [i for i in inputfile.lexintervals if i.int_type == IntervalType.general]

    pattern_tokens = tokens(pattern)
    pattern_token_texts = [t[2] for t in pattern_tokens]

    inputfile_tokens = []
    for ti in textintervals:
        tit = inputfile.text[ti.offs:ti.end]
        titt = tokens(tit)
        ti_tokens = [
            (ti.offs + tittn[0], ti.offs + tittn[1], tittn[2])
            for tittn in titt
        ]
        inputfile_tokens += ti_tokens

    inputfile_token_texts = [t[2] for t in inputfile_tokens]

    def jt(token_texts):
        return ' '.join(token_texts)
    jp = jt(pattern_token_texts)

    # -------------------------------
    # Search for pattern

    found = []

    for o in range(len(inputfile_tokens) - len(pattern_tokens)):
        tp = jt(inputfile_token_texts[o:o+len(pattern_tokens)])
        r = util.lratio(jp, tp)
        if r >= ms:
            found.append((o + 1, r))  # TODO: why +1 makes better there?..

    # detect peaks
    peaks = []
    for findex in range(1, len(found) - 1):
        if found[findex-1][1] <= found[findex][1] <= found[findex+1][1]:
            peaks.append(found[findex])

    # filter nearby intersecting peaks, only leave highest
    fpeaks = set(peaks)

    for i1 in range(len(peaks)):
        for i2 in range(len(peaks)):
            o1, r1 = peaks[i1]
            o2, r2 = peaks[i2]
            if peaks[i1] in fpeaks and peaks[i2] in fpeaks and abs(o1 - o2) < len(pattern_tokens) // 2 and r2 < r1:
                fpeaks.remove(peaks[i2])

    # -------------------------------
    # Return results in terms of source doc

    results = []
    for bo, clr in fpeaks:
        cb = inputfile_tokens[bo][0]
        ce = inputfile_tokens[bo + len(pattern_tokens) - 1][1]

        if not marked_tree.search(cb, ce):  # skip results intersecting with already marked
            cwords = inputfile_token_texts[bo:bo+len(pattern_tokens)]
            ctext = inputfile.text[cb:ce]
            results.append((cb, ce - 1, clr, ctext, cwords))

    return results

def organize_search(logger, args):
    import clones
    import itertools

    # default required settings for fuzzy groups
    clones.write_reformatted_sources = False
    clones.checkmarkup = False
    clones.only_generate_for_ui = args.only_ui == "yes"

    inputfile = clones.InputFile(args.input_document)

    fuzzyclonedata = find_like_pattern(inputfile, args.pattern, args.minimal_similarity)

    fgrps = []
    for (cbeg, cend, clr, ctxt, cwrds), ctr in zip(fuzzyclonedata, itertools.count(1)):
        fgrps.append(clones.FuzzyCloneGroup(
            str(ctr), [(0, cbeg, cend)],
            [' '.join(cwrds)], # [ctxt], !! TODO: Don't hack, implement!
            [cwrds],
            ratio=clr
        ))

    clones.initdata([inputfile], fgrps)

def report(logger, args):
    import clones

    clones.FuzzyCloneGroup.reference_text = args.pattern
    fuzzygroups = [clones.VariativeElement([cg]) for cg in clones.clonegroups]
    cohtml = clones.VariativeElement.summaryhtml(fuzzygroups, clones.ReportMode.fuzzymatches)

    outdir = args.output_directory
    with open(os.path.join(outdir, "pyvarelements.html"), 'w', encoding='utf-8') as htmlfile:
        htmlfile.write(cohtml)

    shutil.copyfile(
        os.path.join(os.path.dirname(os.path.abspath(__file__)), 'js', 'interactivity.js'),
        os.path.join(outdir, "interactivity.js")
    )
    shutil.copyfile(
        os.path.join(os.path.dirname(os.path.abspath(__file__)), 'js', 'jquery-2.0.3.min.js'),
        os.path.join(outdir, "jquery-2.0.3.min.js")
    )

    return fuzzygroups


# API
def get_variative_elements(
        input_document: str,
        pattern: str,
        output_directory: str,
        minimal_similarity: float=0.5,
        only_ui: str='yes'
):
    import types
    largs = types.SimpleNamespace()
    largs.minimal_similarity = minimal_similarity
    largs.input_document = input_document
    largs.pattern = pattern
    largs.output_directory = output_directory
    largs.only_ui = only_ui

    organize_search(logger, largs)
    return report(logger, largs)

# command line interface
if __name__ == '__main__':
    args = initargs()
    if args.pattern is None:
        args.pattern = input("Please input what to search for > ")
        print("Ok, searching for: " + args.pattern)
    organize_search(logger, args)
    report(logger, args)
