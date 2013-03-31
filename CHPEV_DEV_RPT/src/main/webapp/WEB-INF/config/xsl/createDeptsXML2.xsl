<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">
	<xsl:element name="tree">
		<xsl:for-each select="Tree/tree">
			<xsl:element name="tree">
						<xsl:attribute name="text"><xsl:value-of select="DEPT_NAME"/></xsl:attribute>
						<xsl:attribute name="target">DeptView</xsl:attribute>
						<xsl:attribute name="src">createSysDeptTreeXML2.rptdo?dept_parent=<xsl:value-of select="DEPT_ID"/></xsl:attribute>
						<xsl:attribute name="action">userAction.rptdo?dept_id=<xsl:value-of select="DEPT_ID"/>&amp;region_id=<xsl:value-of select="AREA_ID"/></xsl:attribute>
						<xsl:if test="FLAG='0'">
						<xsl:attribute name="icon">../images/common/xmltree/xp/dept.gif</xsl:attribute>
						</xsl:if>
						<xsl:if test="FLAG='1'">
						<xsl:attribute name="icon">../images/common/xmltree/xp/dept_inactive.gif</xsl:attribute>
						</xsl:if>
						<xsl:attribute name="openIcon">../images/common/xmltree/xp/deptOpen.gif</xsl:attribute>
						<xsl:attribute name="fileIcon">../images/common/xmltree/xp/deptdef.gif</xsl:attribute>
			</xsl:element>
		</xsl:for-each>
	</xsl:element>
</xsl:template>
</xsl:stylesheet>