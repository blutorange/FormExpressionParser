/*
Language: Form Expression Language
Requires:
Author: Andre Wachsmuth <awa@xima.de>
Contributors: 
Description: A simple interpreted language for use by FORMCYCLE templates.
Category: special-purpose
*/
// Highlighter for highlight.js
// See https://highlightjs.org/
function(hljs) {
  var PREPROCESSOR = {
    className: 'meta',
    begin: /(((%\])|^)[^]*?\[%[%@$=]*)|(%\](?![^]*\[%)[^]*$)/,
    relevance: 10
  };
  var PREPROCESSOR_END = {
    className: 'meta',
    begin: /%\]/,
  };
  var VARIABLE = {
    begin: /[a-zA-Z_][a-zA-Z0-9_]*/,
  };
  var SCOPED_VARIABLE = {
    className: 'symbol',
    begin: /[a-zA-Z_][a-zA-Z0-9_]*::[a-zA-Z_][a-zA-Z0-9_]*/,
    relevance: 0
  };
  var REGEXP = {
    className: 'regexp',
    contains: [hljs.BACKSLASH_ESCAPE],
    begin: '#',
    end: /#[msi]*/,
    relevance: 10
  };
  var FUNCTION =       {
    className: 'function',
    beginKeywords: 'function',
    end: /[;{]/,
    excludeEnd: true,
    contains: [
      hljs.UNDERSCORE_TITLE_MODE,
      {
        className: 'params',
        begin: '\\(',
        end: '\\)',
        contains: [
          VARIABLE,
          hljs.C_BLOCK_COMMENT_MODE,
        ]
      }
    ]
  };
  var NUMBER = {
    className: 'number',
    begin: /([0-9]+\.[0-9]*([eE][-+]?[0-9]+)?)|(\.[0-9]+([eE][-+]?[0-9]+)?)|([0-9]+([eE][-+]?[0-9]+)?)/,
    relevance: 0
  };
  return {
    aliases: ['fep'],
    keywords: {
      keyword: 'if do for try with else case break while catch throw return switch loginfo|2 logwarn|2 default continue logerror|2 logdebug|2',
      literal: 'true false null exception ->'
    },
    contains: [
      PREPROCESSOR,
      hljs.APOS_STRING_MODE,
      hljs.QUOTE_STRING_MODE,
      hljs.C_BLOCK_COMMENT_MODE,
      FUNCTION,
      SCOPED_VARIABLE,
      REGEXP,
      NUMBER,
    ],
  };
}
