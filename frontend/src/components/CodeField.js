import React, { forwardRef } from 'react';
import AceEditor from 'react-ace';
import 'ace-builds/src-noconflict/mode-java';
import 'ace-builds/src-noconflict/theme-terminal';
import 'ace-builds/src-noconflict/theme-mono_industrial';
import 'ace-builds/src-noconflict/ext-language_tools';
import 'ace-builds/src-noconflict/ext-beautify';

const CodeField = forwardRef(({theme, readOnly, placeholder} , ref) => {
  return (
    <AceEditor
      mode="java"
      highlightActiveLine={true}
      theme={theme}
      readOnly={readOnly}
      placeholder={placeholder}
      ref={ref}
      value={`public class Main {
  public static void main(String[] args) {
    System.out.println("Hello, World!");
  }
}`}
      setOptions={{
        fontSize: "12pt",
        enableBasicAutocompletion: true,
        enableLiveAutocompletion: true,
        enableSnippets: true,
        showLineNumbers: true,
        tabSize: 2,
      }}
      width="100%"
      height="500px"
    />
  );
});

export default CodeField;