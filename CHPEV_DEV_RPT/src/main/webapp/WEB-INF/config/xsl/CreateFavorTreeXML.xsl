<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
	<xsl:element name="tree">
		<xsl:for-each select="Tree/tree">
			<xsl:element name="tree">
				<xsl:choose>
					<xsl:when test="ISRESOURCE='N'">
						<xsl:attribute name="text"><xsl:value-of select="RES_NAME"/></xsl:attribute>
						<xsl:attribute name="target">work_frame</xsl:attribute>
						<xsl:attribute name="src">createFavorTreeXML.rptdo?favor_id=<xsl:value-of select="RES_ID"/></xsl:attribute>
						<xsl:attribute name="action">javascript:parent.favorDefPage.pageRedirect_favor('<xsl:value-of select="RES_ID"/>','<xsl:value-of select="RES_NAME"/>')</xsl:attribute>
						<xsl:attribute name="icon">../images/common/xmltree/favorite/folder.gif</xsl:attribute>
						<xsl:attribute name="openIcon">../images/common/xmltree/favorite/folderopen.gif</xsl:attribute>
						<xsl:attribute name="fileIcon">../images/common/xmltree/favorite/file.gif</xsl:attribute>
					</xsl:when>
				</xsl:choose>				
				<xsl:choose>
					<xsl:when test="ISRESOURCE='Y'">
						<xsl:attribute name="text"><xsl:value-of select="RES_NAME"/></xsl:attribute>
						<xsl:attribute name="target">work_frame</xsl:attribute>
						<xsl:attribute name="action">javascript:parent.favorDefPage.pageRedirect_res('<xsl:value-of select="RES_ID"/>','<xsl:value-of select="RES_NAME"/>')</xsl:attribute>
						<xsl:attribute name="icon">../images/common/xmltree/xp/page.gif</xsl:attribute>
					</xsl:when>
				</xsl:choose>
			</xsl:element>
		</xsl:for-each>
	</xsl:element>
</xsl:template>
</xsl:stylesheet>