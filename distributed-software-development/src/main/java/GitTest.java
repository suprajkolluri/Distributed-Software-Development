import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

public class GitTest {
	public static void main(String[] args) throws Exception {
		try (Repository repository = openJGitCookbookRepository()) {
			try (Git git = new Git(repository)) {
				Iterable<RevCommit> logs = git.log().call();
				int count = 0;
				for (RevCommit rev : logs) {
					// System.out.println("Commit: " + rev /* + ", name: " +
					// rev.getName() + ", id: " + rev.getId().getName() */);
					System.out.println(rev.getAuthorIdent().getName());
					count++;
				}
				System.out.println("Had " + count + " commits overall on current branch");

				logs = git.log().add(repository.resolve("remotes/origin/development")).call();
				count = 0;
				for (RevCommit rev : logs) {
					System.out.println("Development, Commit: " + rev + ", name: dddddddddddddddd"
							+ rev.getAuthorIdent().getName() + ", id: " + rev.getCommitTime());
					count++;
				}
				System.out.println("Had " + count + " commits overall on test-branch");

				logs = git.log().all().call();
				count = 0;
				for (RevCommit rev : logs) {
					System.out
							.println("Commit: " + rev + ", name: " + rev.getName() + ", id: " + rev.getId().getName());
					count++;
				}
				System.out.println("Had " + count + " commits overall in repository");

				logs = git.log()
						// for all log.all()
						.addPath("README.md").call();
				count = 0;
				for (RevCommit rev : logs) {
					// System.out.println("Commit: " + rev /* + ", name: " +
					// rev.getName() + ", id: " + rev.getId().getName() */);
					System.out.println(rev.getAuthorIdent().getEmailAddress());
					System.out.println(rev.getCommitterIdent().getName());
					count++;
				}
				System.out.println("Had " + count + " commits on README.md");

				logs = git.log()
						// for all log.all()
						.addPath("distributed-software-development/pom.xml").call();
				count = 0;
				for (RevCommit rev : logs) {
					System.out.println("Commit: " + rev + ", name: " + rev.getAuthorIdent().getEmailAddress() + ", id: "
							+ rev.getId().getName() + "time: " + rev.getCommitTime());

					count++;
				}
				System.out.println("Had " + count + " commits on pom.xml");
			}
		}
	}

	public static Repository openJGitCookbookRepository() throws IOException {
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		Repository repository = builder.readEnvironment() // scan environment
															// GIT_* variables
				.setGitDir(new File("G:\\project515\\GITRepo\\DSD-Team-Titans\\.git")) // scan
																						// up
																						// the
																						// file
																						// system
																						// tree
				.build();
		System.out.println(repository.getDirectory());
		return repository;

	}
}
