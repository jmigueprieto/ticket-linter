import React from "react";
import DynamicTable from "@atlaskit/dynamic-table";
import Button from "@atlaskit/button";
import Badge from "@atlaskit/badge";
import { Link } from "gatsby";
import { SpotlightTarget } from "@atlaskit/onboarding";

const projects = [
  {
    name: "Tour Project 1",
    lastUpdate: "2020-07-07",
    total: 10,
    violations: 7,
  },
  {
    name: "Tour Project 2",
    lastUpdate: "-",
    total: "N/A",
    violations: "N/A",
  },
  {
    name: "Tour Project 3",
    lastUpdate: "-",
    total: "N/A",
    violations: 0,
  },
];

export const MockProjectsTable = () => {
  const head = createHead();
  const rows = getRows(projects);

  return (
    <SpotlightTarget name="step-2">
      <DynamicTable head={head} rows={rows} isFixedSize isLoading={false} />
    </SpotlightTarget>
  );
};

function getRows(projects) {
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
            project.violations === "N/A" ? (
              project.violations
            ) : (
              <>
                <Badge
                  appearance={project.violations === 0 ? "added" : "important"}
                  max={99}
                >
                  {project.violations}
                </Badge>
              </>
            ),
        },
        {
          key: `actions-${index}`,
          content: (
            <div style={{ textAlign: "left" }}>
              {project.violations > 0 ? (
                <SpotlightTarget name="step-3">
                  <Button
                    component={Link}
                    to="/issues"
                    state={{ project: project }}
                    style={{ cursor: "pointer" }}
                  >
                    View Results
                  </Button>
                </SpotlightTarget>
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
              <SpotlightTarget name={`step-${index}`}>
                <Button appearance="primary" style={{ marginRight: "1rem" }}>
                  Scan Project
                </Button>
              </SpotlightTarget>
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
