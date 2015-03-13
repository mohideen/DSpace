package org.dspace.app.xmlui.aspect.artifactbrowser;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.cocoon.el.objectmodel.ObjectModel;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.dspace.app.webui.util.UIUtil;
import org.dspace.core.Context;
import org.dspace.storage.rdbms.DatabaseManager;
import org.dspace.storage.rdbms.TableRow;
import org.dspace.storage.rdbms.TableRowIterator;

public class EmbargoListHelper
{
    public static final String sql = "   SELECT"
            + "   DISTINCT ON (h.handle) h.handle," + "   i1.item_id,"
            + "   bs.bitstream_id," + "   (SELECT" + "      dc.text_value "
            + "    FROM " + "      metadatavalue dc" + "    WHERE "
            + "      dc.metadata_field_id='64' AND"
            + "      dc.item_id=i1.item_id" + "    LIMIT 1) as title,"
            + "   (SELECT" + "      dc.text_value " + "    FROM "
            + "      metadatavalue dc" + "    WHERE "
            + "      dc.metadata_field_id='2' AND"
            + "      dc.item_id=i1.item_id" + "    LIMIT 1) as advisor,"
            + "   (SELECT" + "       dc.text_value" + "    FROM"
            + "       metadatavalue dc" + "    WHERE"
            + "       dc.metadata_field_id='3' AND"
            + "       dc.item_id=i1.item_id" + "    LIMIT 1) as author,"
            + "   (SELECT" + "       dc.text_value" + "    FROM"
            + "       metadatavalue dc" + "    WHERE"
            + "       dc.metadata_field_id='69' AND"
            + "       dc.item_id=i1.item_id" + "    LIMIT 1) as department,"
            + "   (SELECT" + "       dc.text_value" + "    FROM"
            + "       metadatavalue dc" + "    WHERE"
            + "       dc.metadata_field_id='66' AND"
            + "       dc.item_id=i1.item_id" + "    LIMIT 1) as type,"
            + "   rp.end_date" + " FROM" + "   handle h," + "   item i1, "
            + "   item2bundle i2b1," + "   bundle2bitstream b2b1, "
            + "   bitstream bs, " + "   resourcepolicy rp, "
            + "   epersongroup g " + " WHERE "
            + "   h.resource_id=i1.item_id AND"
            + "   i1.item_id=i2b1.item_id AND"
            + "   i2b1.bundle_id=b2b1.bundle_id AND"
            + "   b2b1.bitstream_id=bs.bitstream_id AND"
            + "   bs.bitstream_id=rp.resource_id AND"
            + "   (rp.end_date > CURRENT_DATE OR"
            + "   rp.end_date IS NULL) AND"
            + "   rp.epersongroup_id = g.eperson_group_id AND"
            + "   g.name = 'ETD Embargo'";

    public static List<TableRow> getEmbargoList(Request request)
            throws SQLException
    {
        Context context = UIUtil.obtainContext(request);

        TableRowIterator tri = DatabaseManager.query(context, sql);
        ArrayList<TableRow> rowList = (ArrayList<TableRow>) tri.toList();
        return rowList;
    }

    public static List getEmbargoList(ObjectModel objectModel, int start,
            int end) throws SQLException
    {
        Request request = ObjectModelHelper.getRequest(objectModel);
        Context context = UIUtil.obtainContext(request);

        TableRowIterator tri = DatabaseManager.query(context, sql);
        ArrayList<TableRow> rowList = (ArrayList<TableRow>) tri.toList();
        return rowList;
    }
}
