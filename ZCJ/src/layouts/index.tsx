import { Outlet } from "umi";
import { App, ConfigProvider } from "antd";
import React from "react";
import "./index.less";

const PrimaryLayout: React.FC = () => {
  return (
    <App>
      <ConfigProvider>
        <Outlet />
      </ConfigProvider>
    </App>
  );
};

export default PrimaryLayout;
