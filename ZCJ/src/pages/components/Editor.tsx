import {
  ProCard,
  ProCardProps,
  ProForm,
  ProFormDigit,
  ProFormText,
} from "@ant-design/pro-components";
import React, { Dispatch, SetStateAction } from "react";
import ListenerEditor from "@/pages/components/ListenerEditor";

const Editor: React.FC<
  ProCardProps & {
    context: [
      Record<string, any> | undefined,
      Dispatch<SetStateAction<Record<string, any> | undefined>>,
    ];
  }
> & {
  isProCard: boolean;
} = (properties) => {
  const [content, setContent] = properties.context;

  return (
    <ProCard direction="column" ghost gutter={[6, 6]} {...properties}>
      {/*  -  */}
      <ProCard ghost direction="row" gutter={[6, 6]}>
        {/*  -  */}
        <ProCard
          title="解码器"
          colSpan={{
            xs: 24,
            md: 12,
          }}
          bordered
          boxShadow
          headerBordered
        >
          <ProForm
            submitter={false}
            onChange={(event: any) => {
              setContent({
                ...content,
                listeners: {
                  ...content?.listeners,
                  interaction: {
                    ...content?.listeners?.interaction,

                    [event.target.id]: event.target.className.includes(
                      "ant-input-number-input",
                    )
                      ? Number(event.target.value)
                      : event.target.value,
                  },
                },
              });
            }}
            initialValues={content?.listeners?.interaction}
          >
            <ProFormText name="fqdn" label="地址" />
          </ProForm>
        </ProCard>
        {/*  -  */}
        <ProCard
          title="环境"
          colSpan={{
            xs: 24,
            md: 12,
          }}
          bordered
          boxShadow
          headerBordered
        >
          <ProForm
            submitter={false}
            onChange={(event: any) => {
              setContent({
                ...content,
                entities: {
                  ...content?.entities,
                  environment: {
                    ...content?.entities?.environment,

                    [event.target.id]: event.target.className.includes(
                      "ant-input-number-input",
                    )
                      ? Number(event.target.value)
                      : event.target.value,
                  },
                },
              });
            }}
            initialValues={content?.entities?.environment}
          >
            <ProFormDigit name="room" label="房间号" />
            <ProFormDigit name="difficulty" label="难度" />
            <ProFormDigit name="magnification" label="倍率" />
          </ProForm>
        </ProCard>
      </ProCard>
      {/*  -  */}
      <ProCard title="事件监听器" bordered boxShadow headerBordered>
        <ProForm
          submitter={false}
          onChange={(event: any) => {
            console.log(event);
            // setContent({
            //   ...content,
            //   listeners: {
            //     ...content?.listeners,
            //     interaction: {
            //       ...content?.listeners?.interaction,
            //
            //       [event.target.id]: event.target.className.includes(
            //           "ant-input-number-input",
            //       )
            //           ? Number(event.target.value)
            //           : event.target.value,
            //     },
            //   },
            // });
          }}
          initialValues={content?.listeners?.interaction}
        >
          <ListenerEditor name="on" />
        </ProForm>
      </ProCard>
    </ProCard>
  );
};

Editor.isProCard = true;

export default Editor;
