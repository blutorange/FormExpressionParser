/* Form Expression Language */
CodeMirror.defineSimpleMode("formexpression-program", {
  // The start state contains the rules that are intially used
  start: [
    {regex: /"(?:[^\\]|\\.)*?(?:"|$)/, token: "string"},
    {regex: /'(?:[^\\]|\\.)*?(?:'|$)/, token: "string"},
    {regex: /#(?:[^\\]|\\.)*?(?:#|$)[msi]*/, token: "string-2"},
    {regex: /(for|switch|do|while)(\s*)(<[a-zA-Z_][a-zA-Z0-9_]*>)/, token: ["keyword", null, "def"]},
    {regex: /(break|continue)(\s+)([a-zA-Z_][a-zA-Z0-9_]*)/, token: ["keyword", null, "def"]},
    {regex: /(function)(\s+)([a-zA-Z_][a-zA-Z0-9_$]*)(\s+)([a-zA-Z_][a-zA-Z0-9_$]*)/, token: ["keyword", null, "meta", null, "variable"]},
    {regex: /(function)(\s+)([a-zA-Z_][a-zA-Z0-9_$]*)/, token: ["keyword", null, "variable"]},
    {regex: /(require)(\s+)(scope)(\s+)([a-zA-Z_][a-zA-Z0-9_$]*)/, token: ["def", null, "def", null, "qualifier"]},
    {regex: /(?:global|scope|require)\b/, token: "def"},
    {regex: /(?:var|boolean|number|string|regex|error|array|hash|method|void)\b/, token: "meta"},
    {regex: /(?:function|with|return|throw|continue|in|break|if|for|while|else|do|try|catch|switch|case|default|loginfo|logwarn|logerror|logdebug)\b/, token: "keyword"},
    {regex: /true|false|null|exception/, token: "atom"},
    {regex: /[-+]?(?:\.\d+|\d+\.?\d*)(?:[eE][-+]?\d+)?/, token: "number"},
    // Single line comment
    {regex: /\/\/.*/, token: "comment"},
    // Multi line comment.
    {regex: /\/\*/, token: "comment", next: "comment"},
    {regex: /[-+\/*=<>!~\?]+/, token: "operator"},
    // Template literal
    {regex: /`/, token: "string", next: "templateliteral"},
    // Indent and dedent properties guide autoindentation
    {regex: /[\{\[\(]/, indent: true},
    {regex: /\}/, dedent: true, pop: true},
    {regex: /[\]\)]/, dedent: true},
    // Scoped variables
    {regex: /([a-zA-Z_][a-zA-Z0-9_]*)(::)/, token: ["qualifier", null]},
    // Normal variables
    {regex: /(\.)([a-zA-Z_][a-zA-Z0-9_]*)/, token: ["operator", "variable-2"]},
    {regex: /[a-zA-Z_][a-zA-Z0-9_$]*/, token: "variable"}
  ],
  // The multi-line comment state.
  comment: [
    {regex: /[^]*?\*\//, token: "comment", next: "start"},
    {regex: /[^]*/, token: "comment"}
  ],
  templateliteral: [
    {regex: /`/, token: "string", next: "start"},
    {regex: /(\$)(\{)/, token: [null, null] , indent: true, push: "start"},
    {regex: /./, token: "string"},
    {regex: /[^]*/, token: "string"}
  ],
  // The meta property contains global information about the mode. It
  // can contain properties like lineComment, which are supported by
  // all modes, and also directives like dontIndentStates, which are
  // specific to simple modes.
  meta: {
    dontIndentStates: ["comment", "templateliteral"],
    lineComment: "//",
	lineSeparator: "\n",
	electricChars: "}])"
  }
});
