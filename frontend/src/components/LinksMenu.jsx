import React from "react";
import { LinkItem, CustomItem, ButtonItem } from "@atlaskit/menu";
import { Link } from "gatsby";
import ArrowRightIcon from "@atlaskit/icon/glyph/arrow-right";
import LinkIcon from "@atlaskit/icon/glyph/link";
import CameraFilledIcon from "@atlaskit/icon/glyph/camera-filled";
import { SpotlightTarget } from "@atlaskit/onboarding";

const LinksMenu = (props) => {
  return (
    <>
      <CustomItem
        to="/rules"
        component={Link}
        iconBefore={<ArrowRightIcon />}
        description="Why we use these rules?"
      >
        Enforced Rules
      </CustomItem>
      <LinkItem
        href="https://www.atlassian.com/agile/project-management/user-stories"
        target="_blank"
        rel="noreferrer"
        iconBefore={<LinkIcon />}
        description="External link"
      >
        User Stories with Examples
      </LinkItem>
      <SpotlightTarget name="start">
        <ButtonItem
          description="Let us show you how this works"
          iconBefore={<CameraFilledIcon />}
          onClick={() => props.start(0)}
        >
          Take a Tour
        </ButtonItem>
      </SpotlightTarget>
    </>
  );
};

export default LinksMenu;
