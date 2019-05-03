/**
 * <NEWLINE>
 * 
 * The Network class implements a neural network.
 * <p>
 * The network consists of three types of neurons: photoreceptors(@see
 * Photoreceptor), interneurons(@see Interneuron) and 6 cortical neurons(@see
 * CorticalNeuron). The network processes light waves. There are three types of
 * photoreceptors, that perceive the different colors.
 * 
 * @author Daniil Sozonchik
 * @version 1.0
 * @since 2019-04-29
 */
import java.util.ArrayList;
import java.util.Arrays;

public class Network {
	/** #Photoreceptors in the network */
	int receptors;
	/** #Cortical neurons in the network */
	int cortical;
	/** All the neurons in the network */
	Neuron[] neurons;
	/** Different receptor types */
	String[] receptortypes = { "blue", "green", "red" };

	/**
	 * Adds neurons to the network.
	 * <p>
	 * Defines the neurons in the network.
	 * 
	 * @param inter
	 *            #Interneurons
	 * @param receptors
	 *            #Photoreceptors
	 * @param cortical
	 *            #CorticalNeurons
	 */
	public Network(int inter, int receptors, int cortical) {
		if (receptors < 3 || inter < receptors){
			throw new RuntimeException();
		}

		this.receptors = receptors;
		this.cortical = cortical;

		neurons = new Neuron [inter + receptors + cortical];

		for (int i = 0; i < neurons.length; i++){
			if (i < receptors){
				int mod = i % 3;
				
				if (mod == 0){
					neurons[i] = new Photoreceptor(i, "blue");
				}
				
				if (mod == 1){
					neurons[i] = new Photoreceptor(i, "green");
				}
				
				if (mod == 2){
					neurons[i] = new Photoreceptor(i, "red");
				}
			}

			else if (i < inter + receptors && i >= receptors){
				neurons[i] = new Interneuron(i);
			}

			else {
				neurons[i] = new CorticalNeuron(i);
			}



		}

	}

	/**
	 * Add a Synapse between the Neurons. The different neurons have their outgoing
	 * synapses as an attribute. ({@link Interneuron}, {@link Photoreceptor},
	 * {@link CorticalNeuron})
	 * 
	 * @param n1
	 *            Presynaptic Neuron (Sender)
	 * @param n2
	 *            Postsynaptic Neuron (Receiver)
	 */

	public void addSynapse(Neuron n1, Neuron n2) {
		Synapse synapse = new Synapse(n1, n2);
		n1.addSynapse(synapse);
		n1.outgoingsynapses.add(synapse);
	}

	/**
	 * Processes the light waves. The lightwaves are integrated by the
	 * photoreceptors (@see Photoreceptor.integrateSignal(double[] signal)) and the
	 * final neural signal is found by summing up the signals in the cortical
	 * neurons(@see CorticalNeuron)
	 * 
	 * @param input
	 *            light waves
	 * @return the neural signal that can be used to classify the color
	 */
	public double[] signalprocessing(double[] input) {
		double signal[];

		for (int i = 0; i < this.receptors; i++){
			//Photoreceptor p = (Photoreceptor) neurons[i]; 
			//p.integrateSignal(input);
			neurons[i].integrateSignal(input);

		}
		
		signal = new double[3];
		double[] tmp = new double[3];

		for (int i = neurons.length - cortical; i < neurons.length; i++){

			CorticalNeuron c= (CorticalNeuron) neurons[i];
			
			tmp = c.getSignal();
			

			for (int j = 0; j<3; j++){
				signal[j] = signal[j]+ (tmp[j] / countColorreceptors()[j]);
			}
		}

		return signal;
	}


	public double[] countColorreceptors() {
		double[] colorreceptors = new double[3];
		Photoreceptor c;
		for (Neuron neuron : this.neurons) {
			if (neuron instanceof Photoreceptor) {
				c = (Photoreceptor) neuron;
				if (c.type == "blue")
					colorreceptors[0]++;
				else if (c.type == "green")
					colorreceptors[1]++;
				else if (c.type == "red")
					colorreceptors[2]++;
			}
		}
		return colorreceptors;
	}

	/**
	 * Classifies the neural signal to a color.
	 * 
	 * @param signal
	 *            neural signal from the cortical neurons
	 * @return color of the mixed light signals as a String
	 */
	public String colors(double[] signal) {
		String color = "grey";
		if (signal[0] > 0.6 && signal[1] < 0.074)
			color = "violet";
		else if (signal[0] > 0.21569 && signal[1] < 0.677)
			color = "blue";
		else if (signal[0] <= 0.21569 && signal[1] > 0.677 && signal[2] > 0.333)
			color = "green";
		else if (signal[1] < 0.713 && signal[2] > 0.913)
			color = "yellow";
		else if (signal[1] > 0.068 && signal[2] > 0.227)
			color = "orange";
		else if (signal[2] > 0.002)
			color = "red";
		return color;
	}

	public static void main(String[] args) {
		Network network = new Network(3, 3, 3);
		double[] input = {600};

		
		network.addSynapse(network.neurons[3], network.neurons[6]);
		network.addSynapse(network.neurons[4], network.neurons[7]);
		network.addSynapse(network.neurons[5], network.neurons[8]);

		
		for (int i = 0; i < network.receptors; i++) {
			network.addSynapse(network.neurons[i], network.neurons[network.receptors + i]);
		}
		
		String color = (String) network.colors(network.signalprocessing(input));
		System.out.println();
		System.out.println(color);

	}

}

