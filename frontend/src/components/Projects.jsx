import React from "react";
import DynamicTable from "@atlaskit/dynamic-table";
import Button from "@atlaskit/button";
import Badge from "@atlaskit/badge";
import { Link } from "gatsby";

export default ({ loading, projects, onEvaluate }) => {
  const head = createHead();
  const rows = getRows(projects, onEvaluate);

  return (
    <DynamicTable head={head} rows={rows} isFixedSize isLoading={loading} />
  );
};

function getRows(projects, onEvaluate) {
  return projects.map((project, index) => {
    return {
      key: `${index}`,
      cells: [
        { content: project.name, key: project.name },
        { content: project.lastUpdate, key: project.lastUpdate },
        { content: project.total, key: project.total },
        {
          key: `violations-${index}`,
          content:
            project.violations === "N/A" || project.violations === 0 ? (
              project.violations
            ) : (
              <>
                <Badge appearance="important" max={99}>
                  {project.violations}
                </Badge>
              </>
            ),
        },
        {
          key: `actions-${index}`,
          content: (
            <div style={{ textAlign: "left"}}>
              {project.violations > 0 ? (
                <Button
                  component={Link}
                  to="/issues"
                  state={{ project: project }}
                  style={{ cursor: "pointer" }}
                >
                  View Results
                </Button>
              ) : (
                "-"
              )}
            </div>
          ),
        },
        {
          key: `options-${index}`,
          content: (
            <div style={{ textAlign: "right" }}>
              <Button
                appearance="primary"
                style={{ marginRight: "1rem" }}
                onClick={() => onEvaluate(project.key)}
              >
                Scan Project
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
        key: "actions",
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
