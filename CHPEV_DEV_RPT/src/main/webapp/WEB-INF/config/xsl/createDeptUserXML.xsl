<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output omit-xml-declaration="yes"/>
<xsl:template match="/">
	<xsl:element name="tree">
		<xsl:for-each select="Tree/tree">
			<xsl:element name="tree">
				<xsl:choose>
					<xsl:when test="CATID='1'">
						<xsl:attribute name="text"><xsl:value-of select="REGION_NAME"/></xsl:attribute>
						<xsl:attribute name="target">work_frame</xsl:attribute>
						<xsl:attribute name="src">createDeptUserXML.rptdo?region_parent=<xsl:value-of select="REGION_ID"/></xsl:attribute>
						<xsl:attribute name="action">javascript:parent.userDefPage.pageRedirect_region('<xsl:value-of select="REGION_ID"/>','<xsl:value-of select="REGION_NAME"/>')</xsl:attribute>
						<xsl:attribute name="icon">../images/common/xmltree/xp/region.gif</xsl:attribute>
						<xsl:attribute name="openIcon">../images/common/xmltree/xp/regionOpen.gif</xsl:attribute>
						<xsl:attribute name="fileIcon">../images/common/xmltree/xp/regiondef.gif</xsl:attribute>
					</xsl:when>
				</xsl:choose>
				<xsl:choose>
					<xsl:when test="CATID='2'">
						<xsl:attribute name="text"><xsl:value-of select="DEPT_NAME"/></xsl:attribute>
						<xsl:attribute name="target">work_frame</xsl:attribute>
						<xsl:attribute name="src">createDeptUserXML.rptdo?dept_parent=<xsl:value-of select="DEPT_ID"/>&amp;region_parent=<xsl:value-of select="REGION_ID"/></xsl:attribute>
						<xsl:attribute name="action">javascript:parent.userDefPage.pageRedirect_dept('<xsl:value-of select="REGION_ID"/>','<xsl:value-of select="REGION_NAME"/>','<xsl:value-of select="DEPT_ID"/>')</xsl:attribute>
						<xsl:attribute name="icon">../images/common/xmltree/xp/dept.gif</xsl:attribute>
						<xsl:attribute name="openIcon">../images/common/xmltree/xp/deptOpen.gif</xsl:attribute>
						<xsl:attribute name="fileIcon">../images/common/xmltree/xp/deptdef.gif</xsl:attribute>
					</xsl:when>
				</xsl:choose>
				<xsl:choose>
					<xsl:when test="CATID='3'">
						<xsl:attribute name="text"><xsl:value-of select="USER_NAME"/></xsl:attribute>
						<xsl:attribute name="target">work_frame</xsl:attribute>
						<xsl:attribute name="action">javascript:parent.userDefPage.pageRedirect_id('<xsl:value-of select="REGION_ID"/>','<xsl:value-of select="REGION_NAME"/>','<xsl:value-of select="USER_ID"/>','<xsl:value-of select="USER_NAME"/>')</xsl:attribute>
						<xsl:attribute name="icon">../images/common/xmltree/xp/userdef.gif</xsl:attribute>
					</xsl:when>
				</xsl:choose>
			</xsl:element>
		</xsl:for-each>
	</xsl:element>
</xsl:template>
</xsl:stylesheet>