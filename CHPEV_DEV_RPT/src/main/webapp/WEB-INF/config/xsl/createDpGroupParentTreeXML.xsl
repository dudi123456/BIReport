<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">
	<xsl:element name="tree">
		<xsl:for-each select="Tree/tree">
			<xsl:element name="tree">				
						<xsl:attribute name="text"><xsl:value-of select="GROUP_NAME"/></xsl:attribute>
						<xsl:attribute name="target">work_frame</xsl:attribute>
						<xsl:attribute name="src">createDpGroupParentTreeXML.rptdo?dept_parent=<xsl:value-of select="GROUP_ID"/></xsl:attribute>
						<xsl:attribute name="action">javascript:getDept('<xsl:value-of select="GROUP_NAME"/>', '<xsl:value-of select="GROUP_ID"/>','<xsl:value-of select="GROUP_NAME"/>','<xsl:value-of select="GROUP_ID"/>');</xsl:attribute>
						<xsl:attribute name="icon">../images/common/xmltree/xp/dept.gif</xsl:attribute>
						<xsl:attribute name="openIcon">../images/common/xmltree/xp/deptOpen.gif</xsl:attribute>
						<xsl:attribute name="fileIcon">../images/common/xmltree/xp/deptdef.gif</xsl:attribute>							
			</xsl:element>
		</xsl:for-each>
	</xsl:element>
</xsl:template>
</xsl:stylesheet>