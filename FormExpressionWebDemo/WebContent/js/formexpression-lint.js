/*
 * CodeMirror.js and lint.js need to be loaded before this script.
 *  
 * Use as follows with code mirror. Set the lint option when
 * initializing code mirror to an object with the entry getAnnotations
 * set to this function; and provide the required options.
 * 
 * For example, here are the options with their default values:
 * 
 *   var myCodeMirror = CodeMirror.fromTextArea($("#code")[0], {
 *      mode: "formexpression-program",
 *      lineSeparator: "\n",
 *      theme: "dracula",
 *      lineNumbers: true,
 *      gutters: ["CodeMirror-lint-markers"],
 *      lint: {
 *          "getAnnotations": CodeMirror.formexpressionValidator,
 *          "async": true,
 *          "type": "program",
 *          "context": "generic",
 *          "strict": "false",
 *          "servlet": "./LintServlet",
 *          "disabled": false
 *      }
 *  })
 *  
 *  Options can be changed on-the-fly as follows:
 *
 *      // Change an option to a different value.
 *      myCodeMirror.getOption("lint").context = "generic"
 *      // Trigger linting to update the editor.
 *  	myCodeMirror.performLint()
 *  
 */
if (typeof CodeMirror === "function") {
	CodeMirror.formexpressionValidator = function(text, updateLinting, options) {
		if (options.disabled)
			return;
		var params = {
			code : text,
			type : options.hasOwnProperty("type") ? String(options.type)
					: "program",
			context : options.hasOwnProperty("context") ? String(options.context)
					: "generic",
			strict : options.hasOwnProperty("strict") ? String(options.strict)
					: "false",
			offsetLineBegin : 1,
			offsetColumnBegin : 1,
			offsetLineEnd : 1,
			offsetColumnEnd : 0,
		}
		if (options.servlet) {
			$.post(options.servlet, params, null, "json").done(
					function(data) {
						if (data.error) {
							console.error("Error during call to lint servlet: "
									+ data.error)
						}
						if (data.lint) {
							updateLinting(data.lint);
						}
					}).fail(function() {
				console.error("Failed to execute post request, try again.")
			})
		}
		else {
			console.log("no servlet provided")
		}
	}
}