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
	var myCodeMirror = CodeMirror.fromTextArea($("#code")[0], {
		mode: "formexpression-program",
		lineSeparator: "\n",
		theme: "dracula",
		indentUnit: 2,
		smartIndent: true,
		tabSize: 4,
		lineNumbers: true,
		onChange: function(){myCodeMirror.save()},
	})
	// Setup saving
	myCodeMirror.setValue(localStorage.code || 'foobar = "Enter your code here...";')
	localStorage.code = myCodeMirror.getValue()
	myCodeMirror.on("change", function(){
		localStorage.code=myCodeMirror.getValue()
	})
	
	var selectTheme = $("#theme")
	var buttonHlgt = $("#buttonHighlight")
	var buttonFmt = $("#buttonFormat")
	var buttonEval = $("#buttonEvaluate")
	
	
    $.each(["3024-day", "3024-night", "abcdef", "ambiance", "ambiance-mobile", "base16-dark", "base16-light", "bespin", "blackboard", "cobalt", "colorforth", "dracula", "eclipse", "elegant", "erlang-dark", "hopscotch", "icecoder", "isotope", "lesser-dark", "liquibyte", "material", "mbo", "mdn-like", "midnight", "monokai", "neat", "neo", "night", "panda-syntax", "paraiso-dark", "paraiso-light", "pastel-on-dark", "railscasts", "rubyblue", "seti", "solarized", "the-matrix", "tomorrow-night-bright", "tomorrow-night-eighties", "ttcn", "twilight", "vibrant-ink", "xq-dark", "xq-light", "yeti", "zenburn"], function(idx, val) {
    	selectTheme.append($("<option></option>").text(val).attr("value",val))
    })
    selectTheme.on("change", function() {
    	myCodeMirror.setOption("theme", selectTheme.val())
    })
	
    $("#type").on("change", function() {
    	if ($(this).val() == "program")
    		myCodeMirror.setOption("mode", "formexpression-program")
    	else
    		myCodeMirror.setOption("mode", "formexpression-template")
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
					myCodeMirror.setValue(data.text);
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
					if ($("#asHtml").prop("checked")) {
						out.append($(data.text))
					}
					else {
						out.append($("<pre></pre>").text(data.text))						
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
		})
	})
	
	setTheme("erlang-dark")
})
