import React from "react";
import DynamicTable from "@atlaskit/dynamic-table";
import styled from "styled-components";
import Button from "@atlaskit/button";
import { Link } from "gatsby";

const Wrapper = styled.div`
  min-width: 600px;
`;

export default ({ loading, projects, onEvaluate }) => {
  const head = createHead(true);
  const rows = getRows(projects, onEvaluate);

  return (
    <section style={{ margin: "2rem 0 1rem 0" }}>
      <Wrapper>
        <DynamicTable head={head} rows={rows} isFixedSize isLoading={loading} />
      </Wrapper>
    </section>
  );
};

function getRows(projects, onEvaluate) {
  return projects.map((project, index) => {
    console.log(project);
    return {
      key: `${index}`,
      cells: [
        { content: project.name, key: `${index}-0` },
        { content: project.lastUpdate, key: `${index}-1` },
        { content: project.total, key: `${index}-2` },
        {
          key: `${index}-3`,
          content:
            project.violations === "N/A" || project.violations === 0 ? (
              project.violations
            ) : (
              <>
                <Link to="/issues" style={{ color: "#DE350B", fontWeight: "bolder" }} state={{ project: project }}>
                  {project.violations} (view)
                </Link>
              </>
            ),
        },
        {
          key: `${index}-4`,
          content: (
            <div style={{ textAlign: "right" }}>
              <Button
                appearance="primary"
                style={{ marginRight: "1rem" }}
                onClick={(e) => {
                  onEvaluate(project.key);
                }}
              >
                Run Evaluation
              </Button>
            </div>
          ),
        },
      ],
    };
  });
}

const createHead = () => {
  return {
    cells: [
      {
        key: "name",
        content: "Project",
        isSortable: true,
        width: 25,
      },
      {
        key: "lastUpdate",
        content: "Last Evaluation",
        shouldTruncate: false,
        isSortable: true,
        width: 25,
      },
      {
        key: "total",
        content: "Stories",
        shouldTruncate: false,
        width: 15,
      },
      {
        key: "violations",
        content: "Violations",
        shouldTruncate: false,
        width: 15,
      },
      {
        key: "options",
        shouldTruncate: false,
        width: 15,
      },
    ],
  };
};
