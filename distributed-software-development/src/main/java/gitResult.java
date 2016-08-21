import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.compress.utils.IOUtils;
import org.eclipse.jgit.api.BlameCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

import edu.asu.se.model.BranchDetails;
import edu.asu.se.model.FileActivityDetails;
import edu.asu.se.model.UserActivityDetails;


public class gitResult {

	/**
	 * @param args
	 * @throws IOException
	 * @throws GitAPIException
	 */
	public static void main(String[] args) throws IOException, GitAPIException {

		FileRepositoryBuilder builder = new FileRepositoryBuilder();

		Repository repository = builder.setGitDir(new File("C:/Users/rathnakarreddy/git/DSD-Team-Titans/.git"))
				.readEnvironment().findGitDir().build();

		listRepositoryContents(repository);

		// Close repo
		repository.close();

	}

	private static void listRepositoryContents(Repository repository) throws IOException, GitAPIException {

		@SuppressWarnings("resource")
		Git git = new Git(repository);
		int count = 0;
		List<Ref> call = git.branchList().setListMode(ListMode.ALL).call();
		// HashSet<Ref> call = new HashSet<Ref> (set1);
		// System.out.println(call);
		// List<Ref> call = git.tagList().;
		HashMap<String, Integer> br = new HashMap<String, Integer>();
		for (Ref ref : call) {
			Ref head = repository.getRef(ref.getName());

			// a RevWalk allows to walk over commits based on some filtering
			// that is
			// defined
			@SuppressWarnings("resource")
			RevWalk walk = new RevWalk(repository);
			RevCommit commit = walk.parseCommit(head.getObjectId());
			RevTree tree = commit.getTree();

			String fullPath = ref.getName();
			Date lastDate = null;
			int index = fullPath.lastIndexOf("/");
			String branchName = fullPath.substring(index + 1);
			//LOC
			BlameCommand blamer = new BlameCommand(repository);
			blamer.setStartCommit(head.getObjectId());
            
			//LOC end

			if (!br.containsKey(branchName)) {
				br.put(branchName, 1);
				//System.out.println(br);
				System.out.println("Having tree: " + tree + branchName);
				int total = 0;
				
				BranchDetails branch = new BranchDetails();
				branch.setBranchName(branchName);
				HashMap<String, Integer> hm = new HashMap<String, Integer>();
				HashMap<String, Date> hstart = new HashMap<String, Date>();
				// Date date =
				// walk.parseCommit(head.getObjectId()).getAuthorIdent().getWhen();
				// RevTag t = new RevTag()
				// Date date =
				// walk.parseTag(ref.getObjectId()).getTaggerIdent().getWhen();
				// Date d1 = tag.getTaggerIdent().getWhen();
				// RevTag d2 = walk.parseTag(ref.getObjectId());

				// System.out.println("created :" + date);
				// now use a TreeWalk to iterate over all files in the Tree
				// recursively
				// you can set Filters to narrow down the results if needed
				@SuppressWarnings("resource")
				TreeWalk treeWalk = new TreeWalk(repository);
				treeWalk.addTree(tree);
				treeWalk.setRecursive(true);
				while (treeWalk.next()) {

					 System.out.println("found: " + treeWalk.getPathString());
					// System.out.println("found: " + treeWalk.getPathString());
					Iterable<RevCommit> logs = git.log().call();
					logs = git.log()
							// for all log.all()
							.addPath(treeWalk.getPathString()).call();
					count = 0;

					Date createdDate = null;
					
					String name = "0";
					String file = treeWalk.getPathString();
					int ind = file.lastIndexOf("/");
					String fileName = file.substring(ind + 1);
					// System.out.println(file+"file");
					// String fileP = treeWalk.getPathString();
					// int indP = fileP.lastIndexOf("/");
					String filePath = file.substring(0, ind + 1);
					// System.out.println(filePath + "fileP");
					for (RevCommit rev : logs) {
						// if
						// (rev.getAuthorIdent().getName().equals("yogaviswani")){

						 System.out.println("Commit: " + rev + ", name: " +
						 rev.getAuthorIdent().getName() + ", id: " +
						 rev.getId().getName() + "time: " +
						 rev.getAuthorIdent().getWhen());
						// }
						if(count == 0){
						createdDate = rev.getAuthorIdent().getWhen();
						name = rev.getAuthorIdent().getName();
						}
						lastDate = rev.getAuthorIdent().getWhen();
						
						if (hm.containsKey(rev.getAuthorIdent().getName())) {
							hm.put(rev.getAuthorIdent().getName(), hm.get(rev.getAuthorIdent().getName()) + 1);
							
							//hstart.put(rev.getAuthorIdent().getName(), rev.getAuthorIdent().getWhen());
							
						} else {
							hm.put(rev.getAuthorIdent().getName(), 1);
							
							//firstDate = createdDate;
							//System.out.println(firstDate);
						}
						if(hstart.containsKey(rev.getAuthorIdent().getName())){
							
							if(hstart.get(rev.getAuthorIdent().getName()).before(lastDate)){
								//System.out.println("test"+firstDate);
								hstart.put(rev.getAuthorIdent().getName(), lastDate);
							}
						}
						else{
							//hstart.put(rev.getAuthorIdent().getName(), 1);
							hstart.put(rev.getAuthorIdent().getName(), rev.getAuthorIdent().getWhen());
						}
						count++;
						
						//LOC
						
						blamer.setFilePath(fileName);
			            BlameResult blame = blamer.call();
			    
			            // read the number of lines from the commit to not look at changes in the working copy
			            
			    
			            //System.out.println("Displayed commits responsible for " + lines + " lines of README.md");
						//LOCEND
					}
					System.out.println("Had " + count + " commits on " +
					treeWalk.getPathString());
					System.out.println(name);
					System.out.println(createdDate);
					

					FileActivityDetails f = new FileActivityDetails();
					f.setFileName(fileName);
					f.setFilePath(filePath);
					f.setCreatedDate(createdDate);
					// System.out.println(createdDate);
					f.setCommits(count);
					f.setLastCommittedBy(name);
					// System.out.println(name);

					total += count;

				}
				System.out.println(total);
				System.out.println("key=" + hstart);
				
				UserActivityDetails u = new UserActivityDetails();
				
				for(Entry<String, Integer> each:hm.entrySet()){
					u.setUserName(each.getKey());
					u.setCommitsMade(each.getValue());
					//System.out.println(hstart);//.get(each.getKey())+"Date");
					u.setStartDate(hstart.get(each.getKey()));
				}
				hm.clear();
				hstart.clear();
			}
		}

	}

	

}