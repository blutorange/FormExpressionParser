package de.xima.fc.form.expression.highlight;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.*;

/**
 * Groups several similar tokens and allows you to specify a style
 * for each group. All methods will only be called once when processing
 * a text and applying syntactical highlighting, so you may create new
 * {@link Style} / {@link Color} objects.
 * @author mad_gaksha
 */
public abstract class ABasicHighlightTheme implements IHighlightTheme {

	@Override
	public Style getStyleForToken(final int tokenType) {
		switch (tokenType) {

		case Break:
		case Continue:
		case Do:
		case Case:
		case Catch:
		case Default:
		case Else:
		case For:
		case Function:
		case If:
		case Return:
		case Switch:
		case Throw:
		case Try:
		case While:
		case With:
			// These are like keywords.
		case LogDebug:
		case LogError:
		case LogInfo:
		case LogWarn:
		case Global:
		case Scope:
		case Require:
		case __Exception:
			return getStyleKeyword();

		case Array:
		case Boolean:
		case __Error:
		case Hash:
		case Method:
		case Number:
		case Regex:
		case String:
		case Var:
		case Void:
		case TypeArray:
		case TypeBoolean:
		case TypeError:
		case TypeHash:
		case TypeMethod:
		case TypeNumber:
		case TypeRegex:
		case TypeString:
		case TypeVar:
		case TypeVoid:
			return getStyleType();

		case Identifier:
			return getStyleIdentifier();

		case IdentifierAfterDot:
			return getStyleAttributeIdentifier();

		case False:
		case True:
			return getStyleBooleanLiteral();

		case Null:
			return getStyleNullLiteral();

		case Float:
		case Integer:
			return getStyleNumberLiteral();


		case LambdaArrow:
			return getStyleLambdaLiteral();

		case BackslashQuotedRegex:
			return getStyleRegexLiteral();
			
		case DoubleQuotedString:
		case SingleQuotedString:
		case TemplateLiteralOpen:
		case TemplateLiteralChars:
		case TemplateLiteralClose:
			return getStyleStringLiteral();

		case BracesClose:
		case BracesOpen:
			return getStyleBraces();
		case BracketClose:
		case BracketOpen:
			return getStyleBracket();
		case ParenClose:
		case ParenOpen:
			return getStyleParenthesis();

		case Ampersand:
		case AngleClose:
		case TypeAngleClose:
		case AngleOpen:
		case TypeAngleOpen:
		case Bar:
		case Circumflex:
		case Dash:
		case DoubleAmpersand:
		case DoubleBar:
		case DoubleDash:
		case DoublePlus:
		case DoubleStar:
		case Exclamation:
		case Percent:
		case Plus:
		case Slash:
		case Star:
		case TemplateLiteralInlineOpen:
			return getStyleOperator();

		case AmpersandEqual:
		case AngleCloseEqual:
		case AngleOpenEqual:
		case BarEqual:
		case CircumflexEqual:
		case DashEqual:
		case DoubleAmpersandEqual:
		case DoubleAngleCloseEqual:
		case DoubleAngleOpenEqual:
		case Equal:
		case DoubleBarEqual:
		case DoubleStarEqual:
		case ExclamationDoubleEqual:
		case ExclamationEqual:
		case PercentEqual:
		case PlusEqual:
		case SlashEqual:
		case StarEqual:
		case TripleEqual:
			return getStyleOperatorEqual();

		case QuestionMark:
		case Dot:
		case SemiColon:
		case Colon:
		case Comma:
		case TripleDot:
		case TypeTripleDot:
		case TypeComma:
		case ScopeSeparator:
			return getStylePunctuation();

		case LosChar:
			return getStyleLosBody();

		case LosBodyClose:
		case LosOpen:
			return getStyleLosSeparator();

		case SingleLineComment:
		case MultiLineComment:
		case TypeMultiLineComment:
		case TypeSingleLineComment:
			return getStyleComment();

		default:
			return null;
		}
	}

	protected abstract Style getStyleKeyword();
	protected abstract Style getStyleType();
	protected abstract Style getStyleIdentifier();
	/** @return Style for identifiers after a dot, eg. <code>myVar.someAttribute()</code>.*/
	protected abstract Style getStyleAttributeIdentifier();

	protected abstract Style getStyleBooleanLiteral();
	protected abstract Style getStyleNullLiteral();
	protected abstract Style getStyleNumberLiteral();
	protected abstract Style getStyleStringLiteral();
	protected abstract Style getStyleRegexLiteral();
	/** @return Style for the lambda arrow <code>-></code>. */
	protected abstract Style getStyleLambdaLiteral();

	protected abstract Style getStyleBracket();
	protected abstract Style getStyleBraces();
	protected abstract Style getStyleParenthesis();

	protected abstract Style getStyleOperator();
	protected abstract Style getStyleOperatorEqual();

	protected abstract Style getStyleLosBody();
	protected abstract Style getStyleLosSeparator();

	/** @return Style for colon, dot, comma, semicolon, question mark. */
	protected abstract Style getStylePunctuation();
	protected abstract Style getStyleComment();
}
