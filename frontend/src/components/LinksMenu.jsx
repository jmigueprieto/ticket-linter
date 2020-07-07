import React from "react";
import { LinkItem, CustomItem } from "@atlaskit/menu";
import { Link } from "gatsby";
import ArrowRightIcon from '@atlaskit/icon/glyph/arrow-right';
import LinkIcon from '@atlaskit/icon/glyph/link';
const LinksMenu = () => {
  return (
    <>
      <CustomItem to="/rules" component={Link}
       iconBefore={<ArrowRightIcon/>}
       description="Why we use these rules?"
      >
        Enforced Rules
      </CustomItem>
      <LinkItem
        href="https://www.atlassian.com/agile/project-management/user-stories"
        target="_blank"
        rel="noreferrer"
        iconBefore={<LinkIcon/>}
      description="External link"
      >
        User Stories with Examples 
      </LinkItem>
    </>
  );
};

export default LinksMenu;
