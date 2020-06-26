import React from "react";
import DynamicTable from "@atlaskit/dynamic-table";
import Button from "@atlaskit/button";
import { Link } from "gatsby";

export default ({ loading, projects, onEvaluate }) => {
  const head = createHead();
  const rows = getRows(projects, onEvaluate);

  return <DynamicTable head={head} rows={rows} isFixedSize isLoading={loading} />;
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
