<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">
	<xsl:element name="tree">
		<xsl:for-each select="Tree/tree">
			<xsl:element name="tree">
				<xsl:choose>
					<xsl:when test="CATID='0'">
						<xsl:attribute name="text"><xsl:value-of select="CODE_NAME"/></xsl:attribute>
						<xsl:attribute name="target">work_frame</xsl:attribute>
						<xsl:attribute name="src">createUserGroupXML.rptdo?group_type=<xsl:value-of select="CODE_ID"/>&amp;oper_type=1</xsl:attribute>
						<xsl:attribute name="action">javascript:alert('请选择下级用户组')</xsl:attribute>
						<xsl:attribute name="icon">../images/common/xmltree/xp/region.gif</xsl:attribute>
						<xsl:attribute name="openIcon">../images/common/xmltree/xp/regionOpen.gif</xsl:attribute>
						<xsl:attribute name="fileIcon">../images/common/xmltree/xp/regiondef.gif</xsl:attribute>
					</xsl:when>
				</xsl:choose>				
				<xsl:choose>
					<xsl:when test="CATID='1'">
						<xsl:attribute name="text"><xsl:value-of select="GROUP_NAME"/></xsl:attribute>
						<xsl:attribute name="target">work_frame</xsl:attribute>
						<xsl:attribute name="action">javascript:parent.groupDefPage.pageRedirect_id('<xsl:value-of select="GROUP_ID"/>','<xsl:value-of select="GROUP_NAME"/>','<xsl:value-of select="PARENT_ID"/>')</xsl:attribute>
						<xsl:if test="status='1'">
							<xsl:attribute name="icon">../images/common/xmltree/xp/roledef.gif</xsl:attribute>
						</xsl:if>
						<xsl:if test="status='0'">
							<xsl:attribute name="icon">../images/common/xmltree/xp/roledef_inactive.gif</xsl:attribute>
						</xsl:if>
					</xsl:when>
				</xsl:choose>
						
			</xsl:element>
		</xsl:for-each>
	</xsl:element>
</xsl:template>
</xsl:stylesheet>