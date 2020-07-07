import React from "react";
import { Grid, GridColumn } from "@atlaskit/page";
import { AkCodeBlock } from "@atlaskit/code";
import Layout from "../components/Layout";
import { Link } from "@reach/router";
import sampleStory from "../images/sample-story.png";

const exampleCodeBlock = `
    “As a [persona], I [want to], [so that].”

    AC
    - Acceptance Criteria 1
    - Acceptance Criteria 2

`;

const RulesPage = () => {
  return (
    <Layout>
      <Grid spacing="comfortable">
        <GridColumn>
          <Link to="/projects">Back</Link>
          <h1>Enforced Rules</h1>
          <hr/>
          <p style={{ margin: "20px 0" }}>
            The description of your Jira User Stories should be written
            following the well-known{" "}
            <a
              href="https://www.atlassian.com/agile/project-management/user-stories"
              target="_blank"
              rel="noreferrer"
            >
              User Story template
            </a>{" "}
            and include an Acceptance Criteria which should be a bullet list of
            items that must be fulfilled to consider the story as done.
          </p>
          <p style={{ width: "40%", margin: "20px auto" }}>
            <AkCodeBlock
              mode="dark"
              language="text"
              text={exampleCodeBlock}
              showLineNumbers={false}
            />
          </p>
          <p style={{ margin: "20px 0" }}>
            The user story must be written inside the <b>description</b> text
            area in order to be evaluated.
          </p>
          <p style={{ margin: "30px 0" }} >
            <h3>Example</h3>
            <div style={{ width: "70%", margin: "0 auto" }}>
              <img
                width="100%"
                src={sampleStory}
                alt="User Story Example using the provided format"
              />
            </div>
          </p>
        </GridColumn>
      </Grid>
    </Layout>
  );
};

export default RulesPage;
