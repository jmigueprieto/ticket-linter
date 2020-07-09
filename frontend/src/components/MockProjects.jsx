import React from "react";
import DynamicTable from "@atlaskit/dynamic-table";
import Button from "@atlaskit/button";
import Badge from "@atlaskit/badge";
import { Link } from "gatsby";
import { SpotlightTarget, Modal } from "@atlaskit/onboarding";
import linterResultsImg from "../images/linter-results.png";

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

export const MockProjectsTable = ({ active, finish }) => {
  const head = createHead();
  const rows = getRows(projects);
  return (
    <>
      {active !== 4 ? (
        <SpotlightTarget name="scan-results">
          <DynamicTable head={head} rows={rows} isFixedSize isLoading={false} />
        </SpotlightTarget>
      ) : (
        <Modal
          actions={[{ onClick: finish, text: "End Tour" }]}
          heading="View Project Resuls"
          key="project-results"
          style={{ textAlign: "left" }}
          width={900}
        >
          <p>
            Once Clicked on the View Results button you will be taken to the
            issues detail page.
          </p>
          <img
            width="100%"
            src={linterResultsImg}
            alt="Story Evaluation Results"
          />
          <span style={{ textAlign: "left" }}>
          <h4>In this page you will be able to see: </h4>
            <ul>
              <li>Which stories are well formed </li>
              <li>Which ones violated the rules </li>
              <li>A link to the story details </li>
            </ul>
          </span>
        </Modal>
      )}
    </>
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
                <SpotlightTarget name="view-results">
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
              {index === 0 ? (
                <SpotlightTarget name="scan-project">
                  <Button appearance="primary" style={{ marginRight: "1rem" }}>
                    Scan Project
                  </Button>
                </SpotlightTarget>
              ) : (
                <Button appearance="primary" style={{ marginRight: "1rem" }}>
                  Scan Project
                </Button>
              )}
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
