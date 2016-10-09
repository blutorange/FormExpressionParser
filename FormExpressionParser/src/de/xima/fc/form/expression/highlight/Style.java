package de.xima.fc.form.expression.highlight;

import javax.annotation.concurrent.Immutable;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

@Immutable
public class Style {
	/** Text color */
	public final Color color;
	/** Text size in units (usually pixels).*/
	public final Size size;
	/** Font weight. */
	public final Weight weight;
	/** Whether the text is cursive. */
	public final ImmutableSet<Feature> featureSet;

	public Style() {
		this(Color.BLACK);
	}	
	public Style(Color color) {
		this(color, Weight.DEFAULT);
	}	
	public Style(Color color, Weight weight) {
		this(color, weight, Size.DEFAULT);
	}
	public Style(Color color, Weight weight, Size size, Feature... featureList) {
		if (color == null) color = Color.BLACK;
		if (size == null) size = Size.DEFAULT;
		if (weight == null) weight = Weight.DEFAULT;
		this.color = color;
		this.size = size;
		this.weight = weight;
		if (featureList.length==0) {
			this.featureSet = ImmutableSet.of();
		}
		else {
			this.featureSet = Sets.immutableEnumSet(featureList[0], featureList);
		}
	}
	@Override
	public String toString() {
		return new StringBuilder().append("Style")
				.append('(')
				.append(color)
				.append(',')
				.append(weight)
				.append(',')
				.append(size)
				.append(',')
				.append(featureSet)
				.append(')')
				.toString();
	}
}