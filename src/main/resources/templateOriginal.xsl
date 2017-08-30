<xsl:stylesheet version="3.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="text"/>
    <xsl:strip-space elements="*"/>

    <xsl:template match="entries">
        <xsl:text>&lt;entries></xsl:text>
        <xsl:apply-templates/>
        <xsl:text>&#10;&lt;/entries></xsl:text>
    </xsl:template>

    <xsl:template match="entry">
        <xsl:text>&#10;    &lt;entry field="</xsl:text>
        <xsl:value-of select="field"/>
        <xsl:text>"></xsl:text>
    </xsl:template>

</xsl:stylesheet>