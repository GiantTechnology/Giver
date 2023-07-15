import { PageContainer, ProCard } from "@ant-design/pro-components";
import { Button, Empty, message, Upload } from "antd";
import React, { useState } from "react";
import { UploadOutlined } from "@ant-design/icons";
import * as yaml from "js-yaml";
import Editor from "@/pages/components/Editor";

const Index: React.FC = () => {
  const [content, setContent] = useState<Record<string, any> | undefined>();

  console.log(content);

  return (
    <PageContainer
      title="Giver"
      subTitle="配置文件编辑器"
      footer={
        content
          ? [
              <Button key="tip" type="link">
                请务必序列化数据后再导出配置文件
              </Button>,
              <Button key="rest" onClick={() => setContent(undefined)}>
                重置
              </Button>,
              <Button
                key="submit"
                type="primary"
                onClick={() => {
                  const payload = new Blob([yaml.dump(content)], {
                    type: "text/yaml",
                  });
                  const at = URL.createObjectURL(payload);
                  const downloader = document.createElement("a");
                  //
                  downloader.href = at;
                  downloader.download = "config.yml";
                  //
                  document.body.appendChild(downloader);
                  downloader.click();

                  document.body.removeChild(downloader);
                  URL.revokeObjectURL(at);
                }}
              >
                导出配置文件
              </Button>,
            ]
          : [
              <Upload
                key="select"
                accept=".yml,.yaml"
                showUploadList={false}
                beforeUpload={(file) => {
                  // 读取文件内容
                  const reader = new FileReader();

                  reader.onload = (event) => {
                    if (event.target?.result) {
                      setContent(
                        yaml.load(event.target.result as string, {
                          filename: file.name,
                          onWarning: console.warn,
                          // listener: console.log,
                        }) as Record<string, any>,
                      );
                    } else {
                      message.error("Configuration file parsing failed.");
                    }
                  };

                  reader.readAsText(file);
                  // 阻止文件上传
                  return false;
                }}
              >
                <Button type="primary" icon={<UploadOutlined />}>
                  导入配置文件
                </Button>
              </Upload>,
            ]
      }
    >
      {content ? (
        <Editor
          context={[content, setContent]}
          style={{
            marginBottom: 64,
          }}
        />
      ) : (
        <ProCard bordered boxShadow headerBordered>
          <Empty
            description="请先导入配置文件"
            style={{
              margin: "64px 0",
            }}
          />
        </ProCard>
      )}
    </PageContainer>
  );
};

export default Index;
