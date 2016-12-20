/**
 * 
 */

function setTypeProgram() {
	$("#type").val("program").trigger("change")
}

function setTypeTemplate() {
	$("#type").val("template").trigger("change")
}

function setTheme(theme) {
	$("#theme").val(theme).trigger("change")
}

$(function(){
	var validator = function(text, updateLinting, options) {
		var params = {
				code: text,
				type: options.hasOwnProperty("type") ? String(options.type) : "program",
				context: options.hasOwnProperty("context") ? String(options.context) : "generic",
				strict: options.hasOwnProperty("strict") ? String(options.strict) : "false",
				offsetLineBegin: 1,
				offsetColumnBegin: 1,
				offsetLineEnd: 1,
				offsetColumnEnd: 0,
		}
		$.post("./LintServlet", params, null, "json")
			.done(function(data) {
				if (data.error) {
					alert("Failed to call lint servlet: " + data.error)
				}
				if (data.lint) {
					updateLinting(data.lint);
				}
			})
			.fail(function() {
				alert("Failed to execute post request, try again.")
			})
	}
	
	var myCodeMirror = CodeMirror.fromTextArea($("#code")[0], {
		mode: "formexpression-program",
		lineSeparator: "\n",
		theme: "dracula",
		indentUnit: 2,
		smartIndent: true,
		tabSize: 4,
		lineNumbers: true,
		onChange: function(){myCodeMirror.save()},
		gutters: ["CodeMirror-lint-markers"],
	    lint: {
	    	"getAnnotations": validator,
	    	"async": true,
	    	"type": "program",
	    	"context": "generic",
	    	"strict": "false"
	    }
	})
	
	DEBUG = myCodeMirror;
	
	// Setup saving
	myCodeMirror.setValue(localStorage.code || 'foobar = "Enter your code here...";')
	localStorage.code = myCodeMirror.getValue()
	myCodeMirror.on("change", function(){
		localStorage.code=myCodeMirror.getValue()
	})
	
	var selectTheme = $("#theme")
	var buttonHlgt = $("#buttonHighlight")
	var buttonFmt = $("#buttonFormat")
	var buttonMin = $("#buttonMinify")
	var buttonEval = $("#buttonEvaluate")
	var controlType = $("#type")
	var controlContext = $("#context")
	var controlStrict = $("#strict")
	
    $.each(["3024-day", "3024-night", "abcdef", "ambiance", "ambiance-mobile", "base16-dark", "base16-light", "bespin", "blackboard", "cobalt", "colorforth", "dracula", "eclipse", "elegant", "erlang-dark", "hopscotch", "icecoder", "isotope", "lesser-dark", "liquibyte", "material", "mbo", "mdn-like", "midnight", "monokai", "neat", "neo", "night", "panda-syntax", "paraiso-dark", "paraiso-light", "pastel-on-dark", "railscasts", "rubyblue", "seti", "solarized", "the-matrix", "tomorrow-night-bright", "tomorrow-night-eighties", "ttcn", "twilight", "vibrant-ink", "xq-dark", "xq-light", "yeti", "zenburn"], function(idx, val) {
    	selectTheme.append($("<option></option>").text(val).attr("value",val))
    })
    selectTheme.on("change", function() {
    	myCodeMirror.setOption("theme", selectTheme.val())
    })
	    
    controlType.on("change", function() {
    	if ($(this).val() == "program")
    		myCodeMirror.setOption("mode", "formexpression-program")
    	else
    		myCodeMirror.setOption("mode", "formexpression-template")
		myCodeMirror.getOption("lint").type = $(this).val()
		myCodeMirror.performLint()
    })
    
    controlContext.on("change", function() {
    	myCodeMirror.getOption("lint").context = $(this).val()
    	myCodeMirror.performLint()
    })
    
    controlStrict.on("change", function() {
    	myCodeMirror.getOption("lint").strict = $(this).prop("checked")
    	myCodeMirror.performLint()
    })
    
	buttonHlgt.on("click", function() {
		var form = $("#codeForm")
		var out = $("#output")
		myCodeMirror.save();
		out.empty().text("working...")
		$.post("./HighlightServlet", form.serialize(), null, "json")
			.done(function(data){
				console.log(data)
				out.empty()
				if (data.error) {
					out.text(data.error)
				}
				else {
					out.append($(data.html))
					out.append($("<style></style>").text(data.css))
				}
			})
			.fail(function(){
				out.empty()
				out.append("Failed to execute post request, try again.")
			})
	})
	buttonFmt.on("click", function() {
		var form = $("#codeForm")
		var out = $("#output")
		myCodeMirror.save();
		out.empty().text("working...")
		$.post("./FormatServlet", form.serialize(), null, "json")
			.done(function(data){
				console.log(data, (data||{}).text)
				out.empty()
				if (data.error) {
					out.text(data.error)
				}
				else {
					myCodeMirror.setValue(data.formatCode);
				}
			})
			.fail(function(){
				out.empty()
				out.append("Failed to execute post request, try again.")
			})
	})
	buttonMin.on("click", function() {
		var form = $("#codeForm")
		var out = $("#output")
		myCodeMirror.save();
		out.empty().text("working...")
		$.post("./UnformatServlet", form.serialize(), null, "json")
			.done(function(data){
				console.log(data, (data||{}).text)
				out.empty()
				if (data.error) {
					out.text(data.error)
				}
				else {
					myCodeMirror.setValue(data.formatCode);
				}
			})
			.fail(function(){
				out.empty()
				out.append("Failed to execute post request, try again.")
			})
	})
	buttonEval.on("click", function() {
		var form = $("#codeForm")
		var out = $("#output")
		myCodeMirror.save();
		out.empty().text("working...")
		$.post("./EvaluateServlet", form.serialize(), null, "json")
			.done(function(data){
				console.log(data)
				out.empty()
				if (data.error) {
					out.append($("<pre></pre>").text(data.error))
				}
				else {
					out.append($("<h1>Object</h1>"))
					out.append($("<pre></pre>").text(data.evalObject))
					out.append($("<h1>Output</h1>"))
					if ($("#asHtml").prop("checked")) {
						out.append($(data.evalOutput))
					}
					else {
						out.append($("<pre></pre>").text(data.evalOutput))						
					}
				}
			})
			.fail(function(){
				out.empty()
				out.append("Failed to execute post request, try again.")
			})
	})
	$(".codeExample").on("click", function() {
		var me = this
		$.get("./example/" + me.getAttribute("data-file") + ".fep", null, null, "text").done(function(data){
			myCodeMirror.setValue(data)
			
			if (me.hasAttribute("data-program")) setTypeProgram()
			else if (me.hasAttribute("data-template")) setTypeTemplate()
			else setTypeProgram()

			if (me.hasAttribute("data-strict-yes")) $("#strict").prop("checked", "checked")
			else if (me.hasAttribute("data-strict-no")) $("#strict").prop("checked", "")
			else $("#strict").prop("checked", "")        
			
			if (me.hasAttribute("data-generic")) $("#context").val("generic")
			else if (me.hasAttribute("data-formcycle")) $("#context").val("formcycle")
			else $("#context").val("generic")
			
			if (me.hasAttribute("data-as-html-yes")) $("#asHtml").prop("checked", "checked")
			else if (me.hasAttribute("data-as-html-no")) $("#asHtml").prop("checked", "")
			else $("#asHtml").prop("checked", "")
			
			if (me.hasAttribute("data-format")) buttonFmt.trigger("click")
			if (me.hasAttribute("data-evaluate")) buttonEval.trigger("click")
			if (me.hasAttribute("data-highlight")) buttonHlgt.trigger("click")
			
			controlType.trigger("change")
			controlContext.trigger("change")
			controlStrict.trigger("change")
		})
	})
	
	setTheme("erlang-dark")
})