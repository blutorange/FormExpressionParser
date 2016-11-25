package de.xima.fc.form.expression.highlight;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.Ampersand;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.AmpersandEqual;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.AngleClose;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.AngleCloseEqual;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.AngleOpen;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.AngleOpenEqual;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.BackslashQuotedRegex;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.Bar;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.BarEqual;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.BracesClose;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.BracesOpen;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.BracketClose;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.BracketOpen;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.Break;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.Case;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.Catch;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.Circumflex;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.CircumflexEqual;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.Colon;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.Comma;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.Continue;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.Dash;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.DashEqual;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.Default;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.Do;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.Dot;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.DoubleAmpersand;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.DoubleAmpersandEqual;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.DoubleAngleCloseEqual;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.DoubleAngleOpenEqual;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.DoubleBar;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.DoubleBarEqual;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.DoubleDash;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.DoublePlus;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.DoubleQuotedString;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.DoubleStar;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.DoubleStarEqual;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.Else;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.Equal;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.Exclamation;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.ExclamationDoubleEqual;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.ExclamationEqual;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.False;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.Float;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.For;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.Function;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.Global;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.Identifier;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.IdentifierAfterDot;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.If;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.Integer;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.LambdaArrow;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.LogDebug;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.LogError;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.LogInfo;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.LogWarn;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.LosBodyClose;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.LosChar;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.LosOpen;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.MultiLineComment;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.Null;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.ParenClose;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.ParenOpen;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.Percent;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.PercentEqual;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.Plus;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.PlusEqual;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.QuestionMark;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.Require;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.Return;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.Scope;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.ScopeSeparator;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.SemiColon;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.SingleLineComment;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.SingleQuotedString;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.Slash;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.SlashEqual;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.Star;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.StarEqual;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.Switch;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.Throw;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.TripleEqual;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.True;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.Try;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.Var;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.While;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.With;
import static de.xima.fc.form.expression.grammar.FormExpressionParserConstants.__Exception;

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
		case Var:
		case __Exception:
			return getStyleKeyword();

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
		case AngleOpen:
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
		case ScopeSeparator:
			return getStylePunctuation();

		case LosChar:
			return getStyleLosBody();

		case LosBodyClose:
		case LosOpen:
			return getStyleLosSeparator();

		case SingleLineComment:
		case MultiLineComment:
			return getStyleComment();

		default:
			return null;
		}
	}

	protected abstract Style getStyleKeyword();
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
