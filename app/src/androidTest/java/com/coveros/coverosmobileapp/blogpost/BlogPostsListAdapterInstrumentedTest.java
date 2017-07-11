package com.coveros.coverosmobileapp.blogpost;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coveros.coverosmobileapp.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Maria Kim
 */

@RunWith(AndroidJUnit4.class)
public class BlogPostsListAdapterInstrumentedTest {
    private static BlogPostsListAdapter blogPostsListAdapter;
    private static BlogPostsListActivity blogPostsListActivity;

    private static int EXPECTED_ID = 0;
    private static int EXPECTED_COUNT = 1;
    private static String EXPECTED_TITLE = "\u201CMy Disdain for the Feline Race--an Autobiography";
    private static String EXPECTED_AUTHORDATE = "Ryan Kenney\nJun 27, 2017";


    @Rule
    public ActivityTestRule<BlogPostsListActivity> blogPostsListActivityRule = new ActivityTestRule<BlogPostsListActivity>(BlogPostsListActivity.class) {
        @Override
        public Intent getActivityIntent() {
            JsonObject blogJson = new Gson().fromJson("{\"id\": 1234, \"author\": 14, \"date\": \"2017-06-27T10:23:18\", \"content\": {\"rendered\": \"&#8220;Cats are objectively the worst animal.\"}, \"title\": {\"rendered\": \"&#8220;My Disdain for the Feline Race--an Autobiography\"}}", JsonObject.class);
            SparseArray authors = new SparseArray();
            authors.append(14, "Ryan Kenney");
            BlogPost blogPost = new BlogPost(blogJson, authors);

            ArrayList<String> blogPostData = new ArrayList<>();
            blogPostData.add(String.valueOf(blogPost.getId()));
            blogPostData.add(blogPost.getTitle());
            blogPostData.add(blogPost.getContent());
            Intent intent = new Intent();
            intent.putStringArrayListExtra("blogPostData", blogPostData);
            return intent;
        }
    };

    @Before
    public void setUp() {

        blogPostsListActivity = blogPostsListActivityRule.getActivity();

        JsonObject blogJson = new Gson().fromJson("{\"id\": 1234, \"author\": 14, \"date\": \"2017-06-27T10:23:18\", \"content\": {\"rendered\": \"&#8220;Cats are objectively the worst animal.\"}, \"title\": {\"rendered\": \"&#8220;My Disdain for the Feline Race--an Autobiography\"}}", JsonObject.class);
        SparseArray authors = new SparseArray();
        authors.append(14, "Ryan Kenney");
        BlogPost blogPost = new BlogPost(blogJson, authors);

        List<BlogPost> blogPosts = new ArrayList<>();
        blogPosts.add(blogPost);
        blogPostsListAdapter = new BlogPostsListAdapter(blogPostsListActivity, R.layout.post_list_text, blogPosts);

    }

    @Test
    public void getItem_checkTitle() {
        String actualTitle = ((BlogPost) blogPostsListAdapter.getItem(0)).getTitle();
        assertEquals("Ryan Kenney is expected.", EXPECTED_TITLE, actualTitle);
    }

    @Test
    public void getItem_checkId() {
        int actualId = (int) blogPostsListAdapter.getItemId(0);
        assertEquals("id 0 expected.", EXPECTED_ID, actualId);
    }

    @Test
    public void getCount_withOneItem() {
        int actualCount = blogPostsListAdapter.getCount();
        assertEquals("Count of 1 expected.", EXPECTED_COUNT, actualCount);
    }

    @Test
    public void getView_withNullConvertView() {
        View view = blogPostsListAdapter.getView(0, null, blogPostsListActivity.getBlogPostsListView());

        TextView title = (TextView) view.findViewById(R.id.title);
        TextView authorDate = (TextView) view.findViewById(R.id.author_date);

        assertNotNull("View should not be null. ", view);
        assertNotNull("Title TextView should not be null. ", title);
        assertNotNull("AuthorDate TextView should not be null. ", authorDate);

        String actualTitle = title.getText().toString();
        assertEquals("Names should match.", EXPECTED_TITLE, actualTitle);

        String actualAuthorDate = authorDate.getText().toString();
        assertEquals("Dates should match.", EXPECTED_AUTHORDATE, actualAuthorDate);

    }

    @Test
    public void getView_withNonNullConvertView() {

        ViewGroup convertView = new LinearLayout(blogPostsListActivity);
        BlogPostsListAdapter.BlogPostHolder blogPostHolder = new BlogPostsListAdapter.BlogPostHolder();
        blogPostHolder.title = new TextView(blogPostsListActivity);
        blogPostHolder.authorDate = new TextView(blogPostsListActivity);
        convertView.setTag(blogPostHolder);

        View view = blogPostsListAdapter.getView(0, convertView, blogPostsListActivity.getBlogPostsListView());

        String actualTitle = blogPostsListAdapter.getBlogPostHolder().title.getText().toString();
        assertEquals("Names should match.", EXPECTED_TITLE, actualTitle);

        String actualAuthorDate = blogPostsListAdapter.getBlogPostHolder().authorDate.getText().toString();
        assertEquals("Dates should match.", EXPECTED_AUTHORDATE, actualAuthorDate);
    }
}