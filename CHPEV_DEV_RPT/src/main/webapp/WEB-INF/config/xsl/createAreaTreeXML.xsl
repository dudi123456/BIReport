<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">
<xsl:element name="tree">
<xsl:for-each select="Tree/tree">
	<xsl:element name="tree">
		<xsl:attribute name="text"><xsl:value-of  select="REGION_NAME"/></xsl:attribute>
		<xsl:attribute name="src">createAreaTreeXML.rptdo?region_parent=<xsl:value-of select="REGION_ID"/>&amp;userid=<xsl:value-of select="OPER_NO"/></xsl:attribute>
		<xsl:attribute name="icon">../images/common/xmltree/xp/region.gif</xsl:attribute>
                <xsl:attribute name="action">javascript:parent.getArea('<xsl:value-of select="REGION_NAME"/>', '<xsl:value-of select="REGION_ID"/>');</xsl:attribute>
                <xsl:attribute name="openIcon">../images/common/xmltree/xp/regionOpen.gif</xsl:attribute>
                <xsl:attribute name="fileIcon">../images/common/xmltree/xp/regiondef.gif</xsl:attribute>
	</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
</xsl:stylesheet>