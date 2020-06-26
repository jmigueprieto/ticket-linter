import React from "react";
import "@atlaskit/css-reset";
import { Helmet } from "react-helmet";
import "@atlaskit/css-reset";
import styled from "styled-components";
import Page from "@atlaskit/page";

const Container = styled.div`
  margin-top: 2rem;
`;

export default ({ children }) => (
  <>
    <Helmet>
      <meta charSet="utf-8" />
      <title>User Story Linter</title>
    </Helmet>
    <Page>
      <Container>{children}</Container>
    </Page>
  </>
);
