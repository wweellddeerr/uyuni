import "swagger-ui-react/swagger-ui.css";

import SwaggerUI from "swagger-ui-react";

import SpaRenderer from "core/spa/spa-renderer";

import json from "./swagger.json";

const Swagger = (props) => <SwaggerUI spec={json} />;

export const renderer = (id: string, props) =>
  SpaRenderer.renderNavigationReact(<Swagger props={props} />, document.getElementById(id));
