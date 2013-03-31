String.prototype.format = function()
{
    var args = arguments;
    return this.replace(/\{(\d+)\}/g, function(m, i, o, n)
    {
        return args[i];
    });
}
String.prototype.trim = function()
{
    return this.replace(/^\s+|\s+$/g, "").replace(/(^　*)|(　*$)/g, "");
};
String.prototype.stripTags = function()
{
    return this.replace(/<\/?[^>]+>/gi, '');
};
String.prototype.escapeHTML = function()
{
    return this.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/ /g, '&nbsp;').replace(/\"/g, '&quot;');
};
String.prototype.unescapeHTML = function(str)
{
    return this.replace(/&amp;/g, '&').replace(/&lt;/g, '<').replace(/&gt;/g, '>').replace(/&nbsp;/g, ' ').replace(/&quot;/g, '"');
};
String.prototype.cutString = function(length)
{
    if (this.bytesLength() > length)
    {
        var tmpstr = "";
        for (var i = 0; i < this.length; i++)
        {
            var tmplen = tmpstr.bytesLength();
            if (tmplen == length)
            {
                break;
            }
            else
            {
                if (tmplen + this.substr(i, 1).bytesLength() > length)
                    tmpstr += " ";
                else
                    tmpstr += this.substr(i, 1);
            }
        }
        return tmpstr;
    }
    else
    {
        return this;
    }
};
String.prototype.bytesLength = function()
{
    var sum = 0;
    for (var i = 0; i < this.length; i++)
    {
        if (
				this.charCodeAt(i) >= 0 &&
				this.charCodeAt(i) <= 255
			)
            sum++;
        else
            sum += 2;
    }
    return sum;
}