<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">
	<xsl:element name="tree">
		<xsl:for-each select="Tree/tree">
			<xsl:element name="tree">
			  <xsl:choose>
					<xsl:when test="CATID='3'">
						<xsl:attribute name="text"><xsl:value-of select="MENU_NAME"/></xsl:attribute>
						<xsl:attribute name="target">work_frame</xsl:attribute>
						<xsl:attribute name="src">CreateMenuRoleXML.rptdo?menu_id=<xsl:value-of select="MENU_ID"/></xsl:attribute>
						<xsl:attribute name="action">resRoleList.jsp?menu_id=<xsl:value-of select="MENU_ID"/></xsl:attribute>
						<xsl:if test="STATUS='1'">
						<xsl:attribute name="icon">../images/common/xmltree/xp/page.gif</xsl:attribute>
						<xsl:attribute name="openIcon">../images/common/xmltree/xp/page.gif</xsl:attribute>
						<xsl:attribute name="fileIcon">../images/common/xmltree/xp/page.gif</xsl:attribute>
						</xsl:if>
						<xsl:if test="STATUS!='1'">
						<xsl:attribute name="icon">../images/common/xmltree/xp/page_inactive.gif</xsl:attribute>
						<xsl:attribute name="openIcon">../images/common/xmltree/xp/page_inactive.gif</xsl:attribute>
						<xsl:attribute name="fileIcon">../images/common/xmltree/xp/page_inactive.gif</xsl:attribute> 
						</xsl:if>
					</xsl:when>
				</xsl:choose>				
			</xsl:element>
		</xsl:for-each>
	</xsl:element>
</xsl:template>
</xsl:stylesheet>