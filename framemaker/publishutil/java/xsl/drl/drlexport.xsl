<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:d="http://math.spbu.ru/drl">
	<!--xmlns="http://docbook.org/ns/docbook" -->
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes" omit-xml-declaration="no"/>
	<xsl:template match="Insert-After">
		<xsl:element name="d:Insert-After">
			<xsl:attribute name="nestid"><xsl:value-of select="@nestid"/></xsl:attribute>
			<xsl:for-each select="*">
				<xsl:choose>
					<xsl:when test="local-name()='FakeTable'">
						<xsl:for-each select="*">
							<xsl:choose>
								<xsl:when test="local-name()='FakeTableBody'">
									<xsl:for-each select="*">
										<xsl:choose>
											<xsl:when test="local-name()='row'">
												<xsl:element name="row">
													<xsl:for-each select="*">
														<xsl:choose>
															<xsl:when test="local-name()='entry'">
																<xsl:if test="* or normalize-space()">
																	<xsl:element name="entry">
																		<xsl:for-each select="*">
																			<xsl:copy-of select="current()"/>
																		</xsl:for-each>
																		<xsl:value-of select="current()"/>
																	</xsl:element>
																</xsl:if>
															</xsl:when>
															<xsl:otherwise>
																<xsl:copy-of select="current()"/>
															</xsl:otherwise>
														</xsl:choose>
													</xsl:for-each>
												</xsl:element>
											</xsl:when>
											<xsl:when test="local-name()='FakeRow'">
												<xsl:for-each select="*">
													<xsl:choose>
														<xsl:when test="local-name()='entry'">
															<xsl:if test="* or normalize-space()">
																<xsl:element name="entry">
																	<xsl:for-each select="*">
																		<xsl:copy-of select="current()"/>
																	</xsl:for-each>
																	<xsl:value-of select="current()"/>
																</xsl:element>
															</xsl:if>
														</xsl:when>
														<xsl:otherwise>
															<xsl:copy-of select="current()"/>
														</xsl:otherwise>
													</xsl:choose>
												</xsl:for-each>
											</xsl:when>
											<xsl:otherwise>
												<xsl:copy-of select="current()"/>
											</xsl:otherwise>
										</xsl:choose>
									</xsl:for-each>
								</xsl:when>
								<xsl:otherwise>
									<xsl:copy-of select="current()"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise>
						<xsl:copy-of select="current()"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
		</xsl:element>
	</xsl:template>
	<xsl:template match="graphic">
		<xsl:element name="graphic">
			<xsl:for-each select="@*">
				<xsl:choose>
					<xsl:when test="name()='entityref'">
					</xsl:when>
					<xsl:otherwise>
						<xsl:copy></xsl:copy>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
			<xsl:variable name="ent"><xsl:value-of select="@entityref"/></xsl:variable>
			<xsl:attribute name="fileref"><xsl:value-of select="unparsed-entity-uri($ent)"/></xsl:attribute>
		</xsl:element>
	</xsl:template>
	<xsl:template match="*">
		<xsl:variable name="newName">
			<xsl:choose>
				<xsl:when test="name()='ProductLine' or 
                      name()='Product' or
                      name()='DocumentationCore' or
                      name()='ProductDocumentation' or
                      name()='InfProduct' or
                      name()='InfElement' or
                      name()='Dictionary' or
                      name()='Entry' or
                      name()='FinalInfProduct' or
                      name()='SetValue' or
                      name()='Adapter' or
                      name()='Replace-Nest' or
                      name()='Insert-Before' or
                      name()='Insert-After' or
                      name()='DirRef' or
                      name()='DictRef' or
                      name()='Directory' or
                      name()='Attr' or
                      name()='DirTemplate' or
                      name()='AttrRef' or
                      name()='InfElemRef' or
                      name()='InfElemRefGroup' or
                      name()='Conditional' or
                      name()='Nest'
                      ">
					<xsl:value-of select="concat('d:', name())"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="name()"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:element name="{$newName}">
			<xsl:for-each select="@*">
				<xsl:copy/>
			</xsl:for-each>
			<xsl:apply-templates/>
		</xsl:element>
	</xsl:template>
</xsl:stylesheet>
