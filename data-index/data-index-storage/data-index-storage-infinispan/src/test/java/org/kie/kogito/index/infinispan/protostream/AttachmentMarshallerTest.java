package org.kie.kogito.index.infinispan.protostream;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import org.infinispan.protostream.MessageMarshaller;
import org.junit.jupiter.api.Test;
import org.kie.kogito.index.model.Attachment;
import org.mockito.InOrder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.kie.kogito.index.infinispan.protostream.AttachmentMarshaller.NAME;
import static org.kie.kogito.index.infinispan.protostream.CommentMarshaller.CONTENT;
import static org.kie.kogito.index.infinispan.protostream.CommentMarshaller.ID;
import static org.kie.kogito.index.infinispan.protostream.CommentMarshaller.UPDATED_AT;
import static org.kie.kogito.index.infinispan.protostream.CommentMarshaller.UPDATED_BY;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AttachmentMarshallerTest {

    String id = UUID.randomUUID().toString();
    String name = "AttachmentName";
    String content = "AttachmentContent";
    String updatedBy = "AttachmentUpdatedBy";
    Date now = new Date();

    @Test
    void testReadFrom() throws IOException {
        MessageMarshaller.ProtoStreamReader reader = mock(MessageMarshaller.ProtoStreamReader.class);

        when(reader.readString(ID)).thenReturn(id);
        when(reader.readString(NAME)).thenReturn(name);
        when(reader.readString(CONTENT)).thenReturn(content);
        when(reader.readString(UPDATED_BY)).thenReturn(updatedBy);
        when(reader.readDate(UPDATED_AT)).thenReturn(now);

        AttachmentMarshaller attachmentMarshaller = new AttachmentMarshaller(null);
        Attachment attachment = attachmentMarshaller.readFrom(reader);

        assertThat(attachment)
                .isNotNull()
                .hasFieldOrPropertyWithValue(ID, id)
                .hasFieldOrPropertyWithValue(NAME, name)
                .hasFieldOrPropertyWithValue(CONTENT, content)
                .hasFieldOrPropertyWithValue(UPDATED_BY, updatedBy)
                .hasFieldOrPropertyWithValue(UPDATED_AT, attachmentMarshaller.dateToZonedDateTime(now));

        InOrder inOrder = inOrder(reader);
        inOrder.verify(reader).readString(ID);
        inOrder.verify(reader).readString(NAME);
        inOrder.verify(reader).readString(CONTENT);
        inOrder.verify(reader).readString(UPDATED_BY);
        inOrder.verify(reader).readDate(UPDATED_AT);
    }

    @Test
    void testWriteTo() throws IOException {
        MessageMarshaller.ProtoStreamWriter writer = mock(MessageMarshaller.ProtoStreamWriter.class);
        AttachmentMarshaller marshaller = new AttachmentMarshaller(null);
        Attachment attachment = Attachment.builder().id(id).name(name).content(content).updatedBy(updatedBy).updatedAt(marshaller.dateToZonedDateTime(now)).build();

        marshaller.writeTo(writer, attachment);

        InOrder inOrder = inOrder(writer);
        inOrder.verify(writer).writeString(ID, id);
        inOrder.verify(writer).writeString(NAME, name);
        inOrder.verify(writer).writeString(CONTENT, content);
        inOrder.verify(writer).writeString(UPDATED_BY, updatedBy);
        inOrder.verify(writer).writeDate(UPDATED_AT, now);
    }

    @Test
    void testMarshaller() {
        AttachmentMarshaller marshaller = new AttachmentMarshaller(null);
        assertThat(marshaller.getJavaClass()).isEqualTo(Attachment.class);
        assertThat(marshaller.getTypeName()).isEqualTo(Attachment.class.getName());
    }
}
