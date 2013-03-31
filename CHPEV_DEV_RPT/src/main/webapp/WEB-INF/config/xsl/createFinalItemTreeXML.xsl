<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="/">
        <xsl:element name="tree">
            <xsl:for-each select="Tree/tree">
                <xsl:element name="tree">
                    <xsl:attribute name="text">
                        <xsl:value-of select="NAME"/>
                    </xsl:attribute>
                    <xsl:attribute name="target">bodyFrame</xsl:attribute>
                    <xsl:attribute name="src">../input/createFinalItemTreeXml.jsp?level=<xsl:value-of select="LVL"/>&amp;code=<xsl:value-of
                            select="ID"/>
                    </xsl:attribute>
                    <xsl:attribute name="action">javascript:getMenu('<xsl:value-of select="NAME"/>', '<xsl:value-of
                            select="ID"/>')
                    </xsl:attribute>
                </xsl:element>
            </xsl:for-each>
        </xsl:element>
    </xsl:template>
</xsl:stylesheet>