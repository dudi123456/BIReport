<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output omit-xml-declaration="yes"/>
<xsl:template match="/">
	<xsl:element name="tree">
		<xsl:for-each select="Tree/tree">
			<xsl:element name="tree">
				<xsl:choose>
					<xsl:when test="CATID='1'">
						<xsl:attribute name="text"><xsl:value-of select="FLOW_NAME"/></xsl:attribute>
						<xsl:attribute name="target">job_frame</xsl:attribute>
						<xsl:attribute name="src">createJobTreeXML.rptdo?flow_id=<xsl:value-of select="FLOW_ID"/></xsl:attribute>
						<xsl:attribute name="action">javascript:alert('请选择ETL作业！')</xsl:attribute>
						<xsl:attribute name="icon">../images/common/xmltree/xp/region.gif</xsl:attribute>
						<xsl:attribute name="openIcon">../images/common/xmltree/xp/regionOpen.gif</xsl:attribute>
						<xsl:attribute name="fileIcon">../images/common/xmltree/xp/regiondef.gif</xsl:attribute>
					</xsl:when>
				</xsl:choose>
				<xsl:choose>
					<xsl:when test="CATID='2'">
						<xsl:attribute name="text"><xsl:value-of select="JOB_NAME"/></xsl:attribute>
						<xsl:attribute name="target">joblist_frame</xsl:attribute>			
						<xsl:attribute name="action">etlJob.rptdo?job_id=<xsl:value-of select="JOB_ID"/></xsl:attribute>
						<xsl:attribute name="icon">../images/common/xmltree/xp/userdef.gif</xsl:attribute>
					</xsl:when>
				</xsl:choose>
			</xsl:element>
		</xsl:for-each>
	</xsl:element>
</xsl:template>
</xsl:stylesheet>