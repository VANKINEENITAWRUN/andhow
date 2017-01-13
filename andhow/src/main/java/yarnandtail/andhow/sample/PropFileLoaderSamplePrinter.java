package yarnandtail.andhow.sample;

import yarnandtail.andhow.AndHow;
import yarnandtail.andhow.Property;
import yarnandtail.andhow.PropertyGroup;
import yarnandtail.andhow.util.TextUtil;
import yarnandtail.andhow.SamplePrinter;

/**
 *
 * @author ericeverman
 */
public class PropFileLoaderSamplePrinter extends BaseSamplePrinter implements SamplePrinter {
	protected PrintFormat format;
	
	public PropFileLoaderSamplePrinter() {
		format = new PrintFormat();
		format.blockCommentStart = null;
		format.blockCommentEnd = null;
		format.blockCommentSeparator = null;
		format.lineCommentPrefix = "#";
		format.lineCommentPrefixSeparator = " ";	//Separate the opening line comment from the text
		format.secondLineIndent = "\t";
		format.hr = TextUtil.repeat("##", 45);
		format.lineWidth = 90;
	}
	
	@Override
	public PrintFormat getFormat() {
		return format;
	}
	
	
	@Override
	public TextBlock getSampleFileStart() {
		return null;
	}
	
	@Override
	public TextBlock getSampleStartComment() {
		TextBlock tb = new TextBlock(true, true);
		tb.addHR();
		tb.addLine("Sample properties file generated by " + AndHow.ANDHOW_NAME);
		tb.addLine(AndHow.ANDHOW_TAG_LINE + "  -  " + AndHow.ANDHOW_URL);
		tb.addHR();
		return tb;
		
	}
	
	@Override
	public TextBlock getActualProperty(Class<? extends PropertyGroup> group, Property prop) throws Exception {
		
		TextBlock tb = new TextBlock(false, false);
		
		String propCanonName = PropertyGroup.getCanonicalName(group, prop);
		
		if (prop.getDefaultValue() != null) {
			tb.addLine(
					TextUtil.format("{} = {}", 
						propCanonName, 
						prop.getDefaultValue())
			);
		} else {
			tb.addLine(
					TextUtil.format("{} = [{}]", 
						propCanonName, 
						prop.getValueType().getDestinationType().getSimpleName())
			);
		}
		
		return tb;
	}
	
	@Override
	public TextBlock getSampleFileEnd() {
		return null;
	}

	
}