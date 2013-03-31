<%@ page import="java.util.BitSet, java.util.Enumeration, java.util.StringTokenizer, java.util.Date" %><%@ page import="java.text.ParseException, java.text.SimpleDateFormat, java.io.IOException" %><%!
// =============================================================================

	// The separator between the different selected values of a list
	static final String C_STR_SEPARATOR = ";";

// =============================================================================

	static final int iCaseDiff = ('a' - 'A');

// =============================================================================

	// The list of characters that are not encoded have been determined
	// by referencing O'Reilly's "HTML: The Definitive Guide" (page 164).
	static BitSet objBitSetCharactersNotToEncode;

	static
	{
		int iCharIndex;

		objBitSetCharactersNotToEncode = new BitSet(256);

		for (iCharIndex = 'a'; iCharIndex <= 'z'; iCharIndex++)
			objBitSetCharactersNotToEncode.set(iCharIndex);

		for (iCharIndex = 'A'; iCharIndex <= 'Z'; iCharIndex++)
			objBitSetCharactersNotToEncode.set(iCharIndex);

		for (iCharIndex = '0'; iCharIndex <= '9'; iCharIndex++)
			objBitSetCharactersNotToEncode.set(iCharIndex);

		// A space needs a special encoding: it should be transformed into a '+'
		// this is done in the method URLEncodeUTF8 below
		objBitSetCharactersNotToEncode.set(' ');
		objBitSetCharactersNotToEncode.set('-');
		objBitSetCharactersNotToEncode.set('_');
		objBitSetCharactersNotToEncode.set('.');
		objBitSetCharactersNotToEncode.set('*');
	} // static code

// =============================================================================

	// Tell if the given value is an item of the array of the selected values
	public String isValuePartOfTheSelection(String strValue, String[] astrSelectedValues)
	{
		if (astrSelectedValues == null)
			return "";

		for (int iSelectedIndex = 0; iSelectedIndex < astrSelectedValues.length; iSelectedIndex++)
			if (strValue.equals(astrSelectedValues[iSelectedIndex]))
				return " selected";

		return "";
	} // method isValuePartOfTheSelection

// =============================================================================

	// Concatenate the given value to the list of the selected values
	public void addToList(String strItem, StringBuffer objStringBufferItemList)
	{
		if (objStringBufferItemList.length() != 0)
			objStringBufferItemList.append(C_STR_SEPARATOR);

		objStringBufferItemList.append(strItem);
	} // method addToList

// =============================================================================

	// Return a string listing the different values of the given array
    // Known limitation: value should not include C_STR_SEPARATOR
	public String convertArrayToString(String[] astr)
	{
		if ((astr == null) || (astr.length == 0))
			return "";

		StringBuffer objStringBuffer = new StringBuffer("");

		for (int iIndex = 0; iIndex < astr.length; iIndex++)
		{
			if (iIndex != 0)
				objStringBuffer.append(C_STR_SEPARATOR);

			objStringBuffer.append(astr[iIndex]);
		} // for (iIndex)

		return objStringBuffer.toString();
	} // method convertArrayToString

// =============================================================================

	// Split delimited string of values into an array
	public String[] splitValues(String strValueList)
	{
		if (strValueList == null)
			return null;

		StringTokenizer strTokenizer = new StringTokenizer(strValueList, C_STR_SEPARATOR);
		int iValueCount = strTokenizer.countTokens();
		String[] astrValues = new String[iValueCount];

		for (int iValueIndex = 0; iValueIndex < iValueCount; iValueIndex++)
			astrValues[iValueIndex] = strTokenizer.nextToken();

		return astrValues;
	} // method splitValues

// =============================================================================

	// Return a non null value for the given string
	public String getNonNullValue(String strParameterValue, String strDefaultValue)
	{
		if (strParameterValue == null)
			strParameterValue = strDefaultValue;
		else if (strParameterValue.equals("null"))
			strParameterValue = strDefaultValue;

		return strParameterValue;
	} // method getNonNullValue

// =============================================================================

	// Return a non null value for the given string
	// (considering that default value is an empty string)
	public String getNonNullValue(String strParameterValue)
	{
		return getNonNullValue(strParameterValue, "");
	} // method getNonNullValue

// =============================================================================

	// Return an array of non null values for the given string array
	public String[] getNonNullValues(String[] astrParameterValues)
	{
		if (astrParameterValues == null)
			astrParameterValues = new String[0];

		return astrParameterValues;
	} // method getNonNullValues


// =============================================================================

	// URL-Encode the given string

	// In JDK 1.4, the method java.net.URLEncoder.encode has a signature (String, String),
	// where the second string is the encoding scheme in which you want to encode the first string

	// In JDK 1.3, the only signature of the method java.net.URLEncoder.encode is (String)
	// where it is assumed you want to get a string in the default platform encoding

	// Since JDK1.3 and UTF-8 are supported, we need a method to encode strings using JDK 1.3 and 1.4,
	// The function URLEncodeUTF8(strToEncode) is equivalent to
	// java.net.URLEncoder.encode(strToEncode, "UTF-8") of the JDK 1.4
	String URLEncodeUTF8(String strToEncode)
	{
		try
		{
			byte[] abyUTF8 = strToEncode.getBytes("UTF-8");
			// The maximal size should be string length * 3 (%XX instead of a single char)
			StringBuffer objStringBuffer = new StringBuffer(abyUTF8.length * 3);

			for (int iByteIndex = 0; iByteIndex < abyUTF8.length; iByteIndex++)
			{
				int iChar = (int) abyUTF8[iByteIndex];

				if (iChar < 0)
					iChar += 256;	// get an unsigned byte value in [0,256[

				// If the character doesn't need encoding,
				if (objBitSetCharactersNotToEncode.get(iChar))
				{
					if (iChar == 0x20)
						// ' ' replaced by '+'
						iChar = 0x2B;

					objStringBuffer.append((char) iChar);
				}
				// Else, the character needs encoding,
				else
				{
					// It should be escaped by %XX (XX is the hex value of the byte)
					objStringBuffer.append('%');

					char cChar = Character.forDigit((abyUTF8[iByteIndex] >> 4) & 0xF, 16);

					if (Character.isLetter(cChar))
						// convert the character to upper case (ex: 'a' -> 'A') as part of the hex value
						cChar -= iCaseDiff;

					objStringBuffer.append(cChar);

					cChar = Character.forDigit(abyUTF8[iByteIndex] & 0xF, 16);

					if (Character.isLetter(cChar))
						// convert the character to upper case (ex: 'a' -> 'A') as part of the hex value
						cChar -= iCaseDiff;

					objStringBuffer.append(cChar);
				} // else of if (objBitSetCharactersNotToEncode.get(iChar))
			} // for (iByteIndex)

			return objStringBuffer.toString();
		}
		catch (java.io.UnsupportedEncodingException objUnsupportedEncodingException)
		{
			System.out.println("UnsupportedEncodingException on " + strToEncode);
			return (java.net.URLEncoder.encode(strToEncode));
		} // catch (java.io.UnsupportedEncodingException)
	} // method URLEncodeUTF8

// =============================================================================
%>