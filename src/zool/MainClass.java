package zool; 
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Iterator;
import java.util.Locale;
import java.util.TreeSet;

import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.topics.TopicInferencer;
import cc.mallet.types.Alphabet;
import cc.mallet.types.FeatureSequence;
import cc.mallet.types.IDSorter;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import cc.mallet.types.LabelSequence;
import cc.mallet.util.Randoms;

public class MainClass {

	public static void main(String[] args) throws IOException {
		if (args.length == 0)
		{
			System.out.println("agrs empty");
			return;
		}
		
		args[0] = "C:\\tatom-master\\tatom-master\\data\\austen-bront-split";
		args[1] = "C:\\mallet-2.0.7\\mallet-2.0.7\\data.mallet";
		
		//Import Files
		/*ImportExample importer = new ImportExample();
		InstanceList instances = importer.readDirectory(new File(args[0]));
		instances.save(new File(args[1]));*/
		
		// Load instances from file
		InstanceList instances = InstanceList.load(new File(args[1]));
		
		
		//  Model-training
		//  Create a model with 100 topics, alpha_t = 0.01, beta_w = 0.01
        //  Note that the first parameter is passed as the sum over topics, while
        //  the second is the parameter for a single dimension of the Dirichlet prior.
		int numTopics = 50;
		int numIterations = 100;
	    ParallelTopicModel model = new ParallelTopicModel(numTopics, 50.0, 0.01);

	    // set Random-seed before load instances into model
        model.setRandomSeed(1);
	    model.addInstances(instances);
	    
	    // Use two parallel samplers, which each look at one half the corpus and combine
        //  statistics after every iteration.
        model.setNumThreads(1);
        
        model.setOptimizeInterval(0);
        model.setBurninPeriod(200);
        
        // Run the model for 50 iterations and stop (this is for testing only, 
        // for real applications, use 1000 to 2000 iterations)
        model.setNumIterations(numIterations);
        
        
        model.estimate();
        
        
        
        // Estimate the topic distribution of the first instance, 
        //  given the current Gibbs state.
        double[] topicDistribution = model.getTopicProbabilities(0);


        // Create a new instance named "test instance" with empty target and source fields.
        /*InstanceList testing = new InstanceList(instances.getPipe());
        Instance tmp = new Instance(instances.get(0).getData().toString(), "tr" , "test instance", null);
        testing.addThruPipe(tmp);*/
        
        InstanceList testing = InstanceList.load(new File("C:\\mallet-2.0.7\\mallet-2.0.7\\test.mallet"));
        

        TopicInferencer inferencer = model.getInferencer();
        inferencer.setRandomSeed(1);
        double[] testProbabilities = inferencer.getSampledDistribution(testing.get(0), 100, 10, 10);
        double[] testProbabilities2 = inferencer.getSampledDistribution(testing.get(0), 100, 10, 10);
         Formatter out = new Formatter(new StringBuilder(), Locale.US);
        for (int i = 0; i < numTopics; i++)
        {
        	out.format("%f\t%f\t%f\n",topicDistribution[i],  testProbabilities[i], testProbabilities2[i]);
        }
        System.out.print(out);
	}
}
