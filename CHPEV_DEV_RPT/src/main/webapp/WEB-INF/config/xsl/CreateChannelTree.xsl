<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">
<xsl:element name="tree">
<xsl:for-each select="Tree/tree">
	<xsl:element name="tree">
				<xsl:choose>
					<xsl:when test="LVL='1'">
						<xsl:attribute name="text"><xsl:value-of  select="NAME"/></xsl:attribute>
						<xsl:attribute name="src">CreateChannelTreeXML.rptdo?parent_channel=<xsl:value-of select="ID"/>&amp;level=<xsl:value-of select="LVL"/></xsl:attribute>
						<xsl:attribute name="icon">../images/common/xmltree/xp/region.gif</xsl:attribute>
        				<xsl:attribute name="action">javascript:parent.getChannelData('<xsl:value-of select="NAME"/>', '<xsl:value-of select="ID"/>','<xsl:value-of select="LVL"/>');</xsl:attribute>
        				<xsl:attribute name="openIcon">../images/common/xmltree/xp/regionOpen.gif</xsl:attribute>
        				<xsl:attribute name="fileIcon">../images/common/xmltree/xp/regiondef.gif</xsl:attribute>
					</xsl:when>
				</xsl:choose>				
				<xsl:choose>
					<xsl:when test="LVL='2'">
						<xsl:attribute name="text"><xsl:value-of  select="NAME"/></xsl:attribute>
						<xsl:attribute name="icon">../images/common/xmltree/xp/region.gif</xsl:attribute>
        				<xsl:attribute name="action">javascript:parent.getChannelData('<xsl:value-of select="NAME"/>', '<xsl:value-of select="ID"/>','<xsl:value-of select="LVL"/>');</xsl:attribute>
        				<xsl:attribute name="openIcon">../images/common/xmltree/xp/regionOpen.gif</xsl:attribute>
        				<xsl:attribute name="fileIcon">../images/common/xmltree/xp/regiondef.gif</xsl:attribute>
					</xsl:when>
				</xsl:choose>
		
	</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
</xsl:stylesheet>