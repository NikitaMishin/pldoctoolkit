<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:d="http://math.spbu.ru/drl"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:resolver="java:org.spbu.pldoctoolkit.graph.util.IdUtil"
    version="2.0"
    extension-element-prefixes="resolver"
    exclude-result-prefixes="xsi">

    <xsl:template match="products">
        <d:Product>
            <xsl:apply-templates select="node() | attribute() | text() | comment()"/>
        </d:Product>
    </xsl:template>

    <xsl:template match="parts">
        <xsl:variable name="type"><xsl:value-of select="@xsi:type"/></xsl:variable>
        <xsl:element name="{$type}">
            <xsl:copy-of select="@*[not(name()='xsi:type')]"/>
            <xsl:apply-templates select="text() | comment() | *"/>
        </xsl:element>
    </xsl:template>                      

    <xsl:template match="infElemRefs">
        <d:InfElemRef>
            <xsl:apply-templates select="node() | attribute() | text() | comment()"/>
        </d:InfElemRef>
    </xsl:template>

    <xsl:template match="groups">
        <d:InfElemRefGroup>
            <xsl:apply-templates select="node() | attribute() | text() | comment()"/>
        </d:InfElemRefGroup>
    </xsl:template>

    <!-- infelem id reference -->
    <xsl:template match="@infelem">
        <xsl:variable name="uri"><xsl:value-of select="."/></xsl:variable>
        <xsl:attribute name="infelemid">
            <xsl:value-of select="resolver:uriStringToId($uri)"/>
        </xsl:attribute>
    </xsl:template>

    <!-- InfElemRef -> group reference -->
    <xsl:template match="@group">
        <xsl:variable name="uri"><xsl:value-of select="."/></xsl:variable>
        <xsl:attribute name="groupid">
            <xsl:value-of select="resolver:uriStringToId($uri)"/>
        </xsl:attribute>
    </xsl:template>
    
    <!-- ProductDocumentation -> Product reference -->
    <!-- FinalInfProduct -> InfProduct reference -->
    <xsl:template match="@product">
        <xsl:param name="finalInfProduct" select="'false'"/>
        <xsl:variable name="uri"><xsl:value-of select="."/></xsl:variable>
        
        <xsl:choose>
            <xsl:when test="$finalInfProduct = 'true'">
                <xsl:attribute name="infproductid">
                    <xsl:value-of select="resolver:uriStringToId($uri)"/>
                </xsl:attribute>
            </xsl:when>
            <xsl:otherwise>
                <xsl:attribute name="productid">
                    <xsl:value-of select="resolver:uriStringToId($uri)"/>
                </xsl:attribute>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="finalInfProducts">
        <d:FinalInfProduct>
            <xsl:apply-templates select="node() | attribute() | text() | comment()">
                <xsl:with-param name="finalInfProduct">true</xsl:with-param>
            </xsl:apply-templates>
        </d:FinalInfProduct>
    </xsl:template>
    
    <!-- Adapters -->
    <xsl:template match="adapters">
        <d:Adapter>
            <xsl:apply-templates select="node() | attribute() | text() | comment()"/>
        </d:Adapter>
    </xsl:template>

    <xsl:template match="node() | attribute() | text() | comment()">
        <xsl:copy>
            <xsl:apply-templates select="node() | attribute() | text() | comment()"/>
        </xsl:copy>
    </xsl:template>
</xsl:stylesheet>
