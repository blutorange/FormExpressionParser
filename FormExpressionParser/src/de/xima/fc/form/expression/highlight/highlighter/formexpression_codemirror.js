/* Form Expression Language */
CodeMirror.defineSimpleMode("formexpression", {
  // The start state contains the rules that are intially used
  start: [
    // The regex matches the token, the token property contains the type
    {regex: /"(?:[^\\]|\\.)*?(?:"|$)/, token: "string"},
    {regex: /'(?:[^\\]|\\.)*?(?:'|$)/, token: "string"},
    {regex: /#(?:[^\\]|\\.)*?(?:#|$)[msi]*/, token: "regexp"},
    // You can match multiple tokens at once. Note that the captured
    // groups must span the whole string in this case
    {regex: /(function)(\s+)([a-zA-Z_][a-zA-Z0-9_]*)/, token: ["keyword", null, "variable-2"]},
    // Rules are matched in the order in which they appear, so there is
    // no ambiguity between this one and the one above
    {regex: /(?:function|with|return|throw|continue|break|if|for|while|else|do|try|catch|switch|case|default|loginfo|logwarn|logerror|logdebug)\b/, token: "keyword"},
    {regex: /true|false|null|exception/, token: "atom"},
    {regex: /([0-9]+\.[0-9]*([eE][-+]?[0-9]+)?)|(\.[0-9]+([eE][-+]?[0-9]+)?)|([0-9]+([eE][-+]?[0-9]+)?)/, token: "number"},
    {regex: /\/\/.*/, token: "comment"},
    // A next property will cause the mode to move to a different state
    {regex: /\/\*/, token: "comment", next: "comment"},
    {regex: /[-+\/*=<>!]+/, token: "operator"},
    // indent and dedent properties guide autoindentation
    {regex: /[\{\[\(]/, indent: true},
    {regex: /[\}\]\)]/, dedent: true},
    {regex: /[a-zA-Z_][a-zA-Z0-9_]*/, token: "variable"},
    // You can embed other modes with the mode property. This rule
    // causes all code between << and >> to be highlighted with the XML
    // mode.
    {regex: /(%\])|^/, token: "meta", mode: {spec: "text", end: /(\[%[%$@=]*)|$/}}
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
    lineComment: "//"
  }
});

