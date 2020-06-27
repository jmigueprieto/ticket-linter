import React from "react";
import DynamicTable from "@atlaskit/dynamic-table";
import CheckCicleIcon from "@atlaskit/icon/glyph/check-circle";
import CrossCircleIcon from "@atlaskit/icon/glyph/cross-circle";

export default ({ issues, loading }) => {
  const head = createHead();
  const rows = issues.map((issue, index) => {
    return {
      key: `${index}`,
      cells: [
        {
          content: (
            <>
              <a href={issue.url} rel="noreferrer" target="_blank" style={{ marginLeft: "5px" }}>
                {issue.key}
              </a>
            </>
          ),
          key: issue.key,
        },
        { content: issue.summary, key: `${index}-1` },
        {
          content: (
            <>
              {issue.validation.isValid ? (
                <CheckCicleIcon primaryColor="#36B37E" />
              ) : (
                <CrossCircleIcon primaryColor="#DE350B" />
              )}
            </>
          ),
          key: `result-${issue.validation.isValid}-${index}`,
        },
        {
          content: (
            <>
              <ul>
                {issue.validation.messages.map((message, index) => (
                  <li key={index}>{message}</li>
                ))}
              </ul>
            </>
          ),
          key: `messages-${index}`,
        },
      ],
    };
  });

  return <DynamicTable head={head} rows={rows} isFixedSize isLoading={loading} />;
};

const createHead = () => {
  return {
    cells: [
      {
        key: "key",
        content: "Story",
        isSortable: true,
        shouldTruncate: false,
        width: 80,
      },
      {
        key: "summary",
        content: "Summary",
        width: 200,
      },
      {
        key: "result",
        content: "Result",
        isSortable: true,
        shouldTruncate: false,
        width: 50,
      },
      {
        key: "messages",
        shouldTruncate: false,
        width: 250,
      },
    ],
  };
};
