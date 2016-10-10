/* Form Expression Language */
CodeMirror.defineSimpleMode("formexpression-program", {
  // The start state contains the rules that are intially used
  start: [
    {regex: /"(?:[^\\]|\\.)*?(?:"|$)/, token: "string"},
    {regex: /'(?:[^\\]|\\.)*?(?:'|$)/, token: "string"},
    {regex: /#(?:[^\\]|\\.)*?(?:#|$)[msi]*/, token: "string-2"},
    {regex: /(for|switch|do|while)(\s*)(<[a-zA-Z_][a-zA-Z0-9_]*>)/, token: ["keyword", null, "def"]},
    {regex: /(break|continue)(\s+)([a-zA-Z_][a-zA-Z0-9_]*)/, token: ["keyword", null, "def"]},
    {regex: /(function)(\s+)([a-zA-Z_][a-zA-Z0-9_$]*)/, token: ["keyword", null, "variable-2"]},    
    {regex: /(?:function|with|return|throw|continue|break|if|for|while|else|do|try|catch|switch|case|default|loginfo|logwarn|logerror|logdebug)\b/, token: "keyword"},
    {regex: /true|false|null|exception/, token: "atom"},
    {regex: /[\d]+|[-+]?(?:\.\d+|\d+\.?\d*)(?:e[-+]?\d+)?/i, token: "number"},
    // Single line comment
    {regex: /\/\/.*/, token: "comment"},
    // Multi line comment.
    {regex: /\/\*/, token: "comment", next: "comment"},
    {regex: /[-+\/*=<>!]+/, token: "operator"},
    // indent and dedent properties guide autoindentation
    {regex: /[\{\[\(]/, indent: true},
    {regex: /[\}\]\)]/, dedent: true},
    // Scoped variables
    {regex: /([a-zA-Z_][a-zA-Z0-9_]*)(::)/, token: ["qualifier", null]},
    // Normal variables
    {regex: /(\.)([a-zA-Z_][a-zA-Z0-9_]*)/, token: ["operator", "variable-3"]},
    {regex: /[a-zA-Z_][a-zA-Z0-9_$]*/, token: "variable"}
  ],
  // The multi-line comment state.
  comment: [
    {regex: /.*?\*\//, token: "comment", next: "start"},
    {regex: /.*/, token: "comment"}
  ],
  // The meta property contains global information about the mode. It
  // can contain properties like lineComment, which are supported by
  // all modes, and also directives like dontIndentStates, which are
  // specific to simple modes.
  meta: {
    dontIndentStates: ["comment"],
    lineComment: "//",
	lineSeparator: "\n",
	electricChars: "}])"
  }
});
