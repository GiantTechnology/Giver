import {
  ProFormSelect,
  ProFormSelectProps,
  ProFormText,
} from "@ant-design/pro-components";
import React, { useState } from "react";
import { TranslationOutlined } from "@ant-design/icons";

const SuperProFormSelect: React.FC<ProFormSelectProps> = (properties) => {
  const [simple, setSimple] = useState<boolean>(false);

  const tooltip = {
    icon: <TranslationOutlined onClick={() => setSimple(!simple)} />,
  };

  return simple ? (
    <>
      <ProFormText
        tooltip={tooltip}
        {...({
          ...properties,
          valueEnum: undefined,
          showSearch: undefined,
        } as any)}
      />
    </>
  ) : (
    <ProFormSelect tooltip={tooltip} {...properties} />
  );
};

export default SuperProFormSelect;
