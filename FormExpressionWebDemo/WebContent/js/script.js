$(function(){
	function setType(type) {
		$("#type").val(type).trigger("change")
	}

	function setTheme(theme) {
		$("#theme").val(theme).trigger("change")
	}

	function setContext(context) {
		$("#context").val(context).trigger("change")
	}

	function setStrict(strict) {
		if (strict)
			$("#strict").prop("checked", "checked")
		else
			$("#strict").prop("checked", "")
		$("#strict").trigger("change")
	}

	function setHtml(html) {
		if (html)
			$("#asHtml").prop("checked", "checked")
		else
			$("#asHtml").prop("checked", "")
		$("#asHtml").trigger("change")
	}
	
	var myCodeMirror = CodeMirror.fromTextArea($("#code")[0], {
		mode: "formexpression-program",
		lineSeparator: "\n",
		theme: "dracula",
		indentUnit: 2,
		smartIndent: true,
		matchBrackets: true,
		autoCloseBrackets: true,
		scrollbarStyle: "overlay",
		tabSize: 4,
		lineNumbers: true,
		onChange: function(){myCodeMirror.save()},
		gutters: ["CodeMirror-lint-markers"],
	    lint: {
	    	"getAnnotations": CodeMirror.formexpressionValidator,
	    	"async": true,
	    	"type": "program",
	    	"context": "generic",
	    	"strict": "false",
	    	"servlet": "./LintServlet",
	    	"disabled": false
	    }
	})
	
	var selectTheme = $("#theme")
	var buttonHlgt = $("#buttonHighlight")
	var buttonFmt = $("#buttonFormat")
	var buttonMin = $("#buttonMinify")
	var buttonEval = $("#buttonEvaluate")
	var controlType = $("#type")
	var controlContext = $("#context")
	var controlStrict = $("#strict")
	var controlHtml = $("#asHtml")
	var controlHeight = $("#height")
	
	var lastChange = 0;
	myCodeMirror.on("change", function(){
		lastChange = new Date().getTime();
		myCodeMirror.getOption("lint").disabled = true
	})
	myCodeMirror.on("cursorActivity", function(){
		lastChange = new Date().getTime();
		myCodeMirror.getOption("lint").disabled = true
	})
	window.setInterval(function(){
		var deltaTime = (new Date().getTime())-lastChange
		if (deltaTime > 1500) {
			var performLint = myCodeMirror.getOption("lint").disabled
			myCodeMirror.getOption("lint").disabled = false
			if (performLint)
				myCodeMirror.performLint()
		}
	}, 1000)	
	
	// Setup saving current code.
	myCodeMirror.setValue(localStorage.code || 'foobar = "Enter your code here...";')
	localStorage.code = myCodeMirror.getValue()
	myCodeMirror.on("change", function(){
		localStorage.code=myCodeMirror.getValue()
	})
    
	// Set theme selection
    $.each(["3024-day", "3024-night", "abcdef", "ambiance", "ambiance-mobile", "base16-dark", "base16-light", "bespin", "blackboard", "cobalt", "colorforth", "dracula", "eclipse", "elegant", "erlang-dark", "hopscotch", "icecoder", "isotope", "lesser-dark", "liquibyte", "material", "mbo", "mdn-like", "midnight", "monokai", "neat", "neo", "night", "panda-syntax", "paraiso-dark", "paraiso-light", "pastel-on-dark", "railscasts", "rubyblue", "seti", "solarized", "the-matrix", "tomorrow-night-bright", "tomorrow-night-eighties", "ttcn", "twilight", "vibrant-ink", "xq-dark", "xq-light", "yeti", "zenburn"], function(idx, val) {
    	selectTheme.append($("<option></option>").text(val).attr("value",val))
    })
    selectTheme.on("change", function() {
    	myCodeMirror.setOption("theme", selectTheme.val())
    })

    // Synchronize options for linting.
    controlType.on("change", function() {
    	if ($(this).val() == "program")
    		myCodeMirror.setOption("mode", "formexpression-program")
    	else
    		myCodeMirror.setOption("mode", "formexpression-template")
		myCodeMirror.getOption("lint").type = $(this).val()
		myCodeMirror.performLint()
        localStorage.type = $(this).val();
    })
    
    controlContext.on("change", function() {
    	myCodeMirror.getOption("lint").context = $(this).val()
    	myCodeMirror.performLint()
        localStorage.context = $(this).val();
    })
    
    controlStrict.on("change", function() {
    	myCodeMirror.getOption("lint").strict = $(this).prop("checked")
    	myCodeMirror.performLint()
        localStorage.strict = $(this).prop("checked");
    })
    
    controlHeight.on("change input", function() {
    	myCodeMirror.setSize("auto", $(this).val())
    })

    controlHtml.on("change", function() {
        localStorage.html = $(this).prop("checked");
    })
    
    // Action buttons
	buttonHlgt.on("click", function() {
		var form = $("#codeForm")
		var out = $("#output")
		myCodeMirror.save();
		out.empty().text("working...")
		$.post("./HighlightServlet", form.serialize(), null, "json")
			.done(function(data){
				out.empty()
				if (data.error) {
					console.error(data)
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
				out.empty()
				if (data.error) {
					console.error(data)
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
				out.empty()
				if (data.error) {
					console.error(data)
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
				out.empty()
				if (data.error) {
					console.error(data)
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
	
	// Code samples.
	$(".codeExample").on("click", function() {
		var me = this
		var dataFile = me.getAttribute("data-file")
		window.location.hash = dataFile
		$.get("./example/" + dataFile + ".fep", null, null, "text").done(function(data){
			myCodeMirror.setValue(data)
			
			if (me.hasAttribute("data-program")) setType("program")
			else if (me.hasAttribute("data-template")) setType("template")
			else setTypeProgram()

			if (me.hasAttribute("data-strict-yes")) setStrict(true)
			else if (me.hasAttribute("data-strict-no")) setStrict(false)
			else setStrict(false)        
			
			if (me.hasAttribute("data-generic")) setContext("generic")
			else if (me.hasAttribute("data-formcycle")) setContext("formcycle")
			else setContext("generic")
			
			if (me.hasAttribute("data-as-html-yes")) setHtml(true)
			else if (me.hasAttribute("data-as-html-no")) setHtml(false)
			else setHtml(false)
			
			if (me.hasAttribute("data-format")) buttonFmt.trigger("click")
			if (me.hasAttribute("data-evaluate")) buttonEval.trigger("click")
			if (me.hasAttribute("data-highlight")) buttonHlgt.trigger("click")
			
			buttonEval.click()
		})
	})
	
	setTheme("erlang-dark")

    // Restore options.
    setType(localStorage.type || "program")
    setContext(localStorage.context || "generic")
    setStrict(localStorage.strict==="true")
    setHtml(localStorage.html==="true")
    
    // Open example corresponding to url hash.
    $("[data-file='" + window.location.hash.substring(1) + "']").click()
})
