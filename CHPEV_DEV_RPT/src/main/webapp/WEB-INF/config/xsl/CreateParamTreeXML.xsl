<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">
	<xsl:element name="tree">
		<xsl:for-each select="Tree/tree">
			<xsl:element name="tree">
				<xsl:choose>
					<xsl:when test="ISLEAF='N'">
						<xsl:attribute name="text"><xsl:value-of select="PARAM_NAME"/></xsl:attribute>
						<xsl:attribute name="target">paramview</xsl:attribute>
						<xsl:attribute name="src">CreateParamTree.rptdo?parent_id=<xsl:value-of select="PARAM_ID"/></xsl:attribute>									
						<xsl:attribute name="action">javascript:alert('请选择叶子结点参数表');</xsl:attribute>
						<xsl:attribute name="icon">../images/common/xmltree/xp/region.gif</xsl:attribute>
						<xsl:attribute name="openIcon">../images/common/xmltree/xp/regionOpen.gif</xsl:attribute>
						<xsl:attribute name="fileIcon">../images/common/xmltree/xp/regiondef.gif</xsl:attribute>
					</xsl:when>
				</xsl:choose>				
				<xsl:choose>
					<xsl:when test="ISLEAF='Y'">
						<xsl:attribute name="text"><xsl:value-of select="PARAM_NAME"/></xsl:attribute>
						<xsl:attribute name="target">paramview</xsl:attribute>						
						<xsl:attribute name="action">ParamQuery.rptdo?clearFlag=1&amp;t_node_id=<xsl:value-of select="PARAM_ID"/>&amp;t_table_name=<xsl:value-of select="TABLE_NAME"/></xsl:attribute>
						<xsl:attribute name="icon">../images/common/xmltree/xp/region.gif</xsl:attribute>						
					</xsl:when>
				</xsl:choose>		
			</xsl:element>
		</xsl:for-each>
	</xsl:element>
</xsl:template>
</xsl:stylesheet>