import React from "react";
import "@atlaskit/css-reset";
import "@atlaskit/reduced-ui-pack";
import { Helmet } from "react-helmet";
import styled from "styled-components";
import Page from "@atlaskit/page";

const Margin = styled.div`
  margin-top: 2rem;
`;

export default ({ children }) => (
  <>
    <Helmet>
      <meta charSet="utf-8" />
      <title>User Story Linter</title>
    </Helmet>
    <Page>
      <Margin>{children}</Margin>
    </Page>
  </>
);
