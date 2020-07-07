import React from "react";
import { Grid, GridColumn } from "@atlaskit/page";
import Layout from "../components/Layout";
import Projects from "../components/Projects";
import LinksMenu from "../components/LinksMenu";

const ProjectsPage = ({ loading, projects, onEvaluate }) => {
  return (
    <Layout>
      <Grid>
        <GridColumn medium={8}>
          <h1>Story Linter</h1>
          <p>
            A Project Scan <span style={{ fontWeight: "bold" }}>evaluates all currently open user stories</span> in the
            project and reports <span style={{ fontWeight: "bold" }}>violations to the rules</span> (e.g. stories that
            are not expressed as expressed as <span style={{ fontStyle: "italic" }}>“persona + need + purpose”</span> or
            don't have an acceptance criteria)
          </p>
        </GridColumn>
        <GridColumn medium={4}>
          <LinksMenu/>
        </GridColumn>
        <GridColumn>
          <div style={{ marginTop: "2rem" }}>
            <Projects projects={projects} loading={loading} onEvaluate={onEvaluate} />
          </div>
        </GridColumn>
      </Grid>
    </Layout>
  );
};

ProjectsPage.defaultProps = {
  loading: true,
  projects: [],
  onEvaluate: () => {},
};

export default ProjectsPage;
