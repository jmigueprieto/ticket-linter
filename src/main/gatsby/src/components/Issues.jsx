import React from "react";
import DynamicTable from "@atlaskit/dynamic-table";
import styled from "styled-components";
import CheckCicleIcon from "@atlaskit/icon/glyph/check-circle";
import CrossCircleIcon from "@atlaskit/icon/glyph/cross-circle";

const Wrapper = styled.div`
  min-width: 600px;
`;

const createHead = (withWidth) => {
  return {
    cells: [
      {
        key: "key",
        content: "Story", //TODO this should be a link to the story
        isSortable: true,
        shouldTruncate: false,
        width: 80,
      },
      {
        key: "summary",
        content: "Summary", //TODO this should be a link to the story
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

export default ({ issues, loading }) => {
  const head = createHead();
  console.log(issues);
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
          key: `key-${index}`,
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
                {issue.validation.messages.map((message) => (
                  <li>{message}</li>
                ))}
              </ul>
            </>
          ),
          key: `messages-${index}`,
        },
      ],
    };
  });

  return (
    <section style={{ margin: "1rem 0 1rem 0" }}>
      <Wrapper>
        <DynamicTable head={head} rows={rows} isFixedSize isLoading={loading} />
      </Wrapper>
    </section>
  );
};
