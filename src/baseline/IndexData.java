package baseline;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

public class IndexData {
	private static IndexWriter indexWriter =null;

	public static void indexDoc(String curSrcFileName, String curSrcSegmentedContent) {
		Document doc=createDocument(curSrcFileName,curSrcSegmentedContent);
		try {
			if (indexWriter.getConfig().getOpenMode() == OpenMode.CREATE) {
				// New index, so we just add the document (no old document can be there):
				indexWriter.addDocument(doc);
			}else {
				// Existing index (an old copy of this document may have been indexed) so we use updateDocument instead to replace the old one matching the exact path, if present:
				indexWriter.updateDocument(new Term("SrcFileName",doc.getFields("SrcFileName").toString()) , doc);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Document createDocument(String srcFileName, String content) {
		Document document = new Document();
		document.add(new StringField("SrcFileName", srcFileName , Field.Store.YES));
		document.add(new TextField("Content", content , Field.Store.NO));
		return document;
	}

	public static void closingIndexWriter() {
		try {
			// NOTE: if you want to maximize search performance, you can optionally call forceMerge here.  This can be a terribly costly operation, so generally it's only worth it when your index is relatively static (ie you're done adding documents to it):
			indexWriter.forceMerge(1);
			indexWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Index writer closed.");
	}

	public static void loadingIndexWriter() {
		System.out.println("Loading Index writer...");
		boolean create=true; 
		try {
			String INDEX_DIR = System.getProperty("user.dir")+"\\Other\\indexDir\\BaselineIndexDir";
			Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));
			Analyzer analyzer = new WhitespaceAnalyzer();
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

			if (create) {
				// Create a new index in the directory, removing any previously indexed documents:
				iwc.setOpenMode(OpenMode.CREATE);
			} else {
				// Add new documents to an existing index:
				iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
			}

			indexWriter = new IndexWriter(dir, iwc);

		} catch (IOException e) {
			System.out.println(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
		}
	}
}
