-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hostiteľ: 127.0.0.1:3306
-- Čas generovania: Št 15.Feb 2024, 15:13
-- Verzia serveru: 8.2.0
-- Verzia PHP: 8.2.13

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Databáza: `eshop`
--

-- --------------------------------------------------------

--
-- Štruktúra tabuľky pre tabuľku `basket`
--

DROP TABLE IF EXISTS `basket`;
CREATE TABLE IF NOT EXISTS `basket` (
  `id` int NOT NULL AUTO_INCREMENT,
  `customer_id` int NOT NULL,
  `product_id` int NOT NULL,
  `price` double NOT NULL,
  `quantity` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `customer_id` (`customer_id`),
  KEY `product_id` (`product_id`)
) ENGINE=MyISAM AUTO_INCREMENT=94 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Štruktúra tabuľky pre tabuľku `orders`
--

DROP TABLE IF EXISTS `orders`;
CREATE TABLE IF NOT EXISTS `orders` (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_number` int NOT NULL,
  `order_date` date NOT NULL,
  `customer_id` int NOT NULL,
  `amount` double NOT NULL,
  `status` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`customer_id`)
) ENGINE=MyISAM AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Sťahujem dáta pre tabuľku `orders`
--

INSERT INTO `orders` (`id`, `order_number`, `order_date`, `customer_id`, `amount`, `status`) VALUES
(31, 1009, '2023-12-24', 25, 21.34, 'confirmed'),
(30, 1008, '2023-12-15', 25, 56.26, 'delivered'),
(29, 1007, '2023-12-14', 26, 15.2, 'delivered'),
(26, 1004, '2023-12-13', 23, 11.16, 'delivered'),
(27, 1005, '2023-12-13', 26, 71.25, 'delivered'),
(28, 1006, '2023-12-13', 26, 13.3, 'delivered'),
(36, 1011, '2024-02-14', 26, 29.44, 'new'),
(38, 1012, '2024-02-15', 26, 90.16, 'new'),
(39, 1013, '2024-02-15', 26, 142.1, 'new'),
(40, 1014, '2024-02-15', 26, 164.64, 'new');

-- --------------------------------------------------------

--
-- Štruktúra tabuľky pre tabuľku `order_items`
--

DROP TABLE IF EXISTS `order_items`;
CREATE TABLE IF NOT EXISTS `order_items` (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_id` int NOT NULL,
  `product_id` int NOT NULL,
  `price` double NOT NULL,
  `quantity` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `product_id` (`product_id`),
  KEY `order_id` (`order_id`)
) ENGINE=MyISAM AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Sťahujem dáta pre tabuľku `order_items`
--

INSERT INTO `order_items` (`id`, `order_id`, `product_id`, `price`, `quantity`) VALUES
(24, 27, 14, 42.75, 5),
(25, 27, 6, 28.5, 2),
(26, 28, 13, 13.3, 1),
(27, 29, 4, 7.6, 1),
(29, 30, 11, 14.55, 1),
(28, 29, 5, 7.6, 1),
(23, 26, 7, 11.16, 1),
(30, 30, 3, 10.67, 1),
(31, 30, 14, 17.46, 2),
(32, 30, 8, 13.58, 1),
(33, 31, 4, 7.76, 1),
(34, 31, 13, 13.58, 1),
(43, 38, 8, 27.44, 2),
(39, 36, 10, 11.04, 1),
(40, 36, 12, 18.4, 2),
(42, 38, 4, 62.72, 8),
(44, 39, 4, 15.68, 2),
(45, 39, 9, 32.34, 3),
(46, 39, 7, 11.76, 1),
(47, 39, 14, 8.82, 1),
(48, 39, 11, 73.5, 5),
(49, 40, 8, 164.64, 12);

-- --------------------------------------------------------

--
-- Štruktúra tabuľky pre tabuľku `products`
--

DROP TABLE IF EXISTS `products`;
CREATE TABLE IF NOT EXISTS `products` (
  `id` int NOT NULL AUTO_INCREMENT,
  `book_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `long_description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `price` decimal(10,0) NOT NULL,
  `picture_name` varchar(100) DEFAULT NULL,
  `author_name` varchar(50) DEFAULT NULL,
  `short_description` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Sťahujem dáta pre tabuľku `products`
--

INSERT INTO `products` (`id`, `book_name`, `long_description`, `price`, `picture_name`, `author_name`, `short_description`) VALUES
(6, 'Ulysses', 'Ulysses is a modernist novel by Irish writer James Joyce. It was first serialized in parts in the American journal The Little Review from March 1918 to December 1920 and then published in its entirety in Paris by Sylvia Beach on 2 February 1922, Joyce\'s 40th birthday. It is considered one of the most important works of modernist literature and has been called \"a demonstration and summation of the entire movement.\"\r\n\r\nThe novel is highly allusive and also imitates the styles of different periods of English literature. Since its publication, the book has attracted controversy and scrutiny, ranging from an obscenity trial in the United States in 1921 to protracted textual \"Joyce Wars.\" The novel\'s stream-of-consciousness technique, careful structuring, and experimental prose—replete with puns, parodies, and allusions—as well as its rich characterisation and broad humour have led it to be regarded as one of the greatest literary works in history; Joyce fans worldwide now celebrate 16 June as Bloomsday.', 15, 'ulysses-james-joyce.jpg', 'James Joyce', 'Follows a day in the life of Leopold Bloom, employing innovative narrative techniques'),
(3, 'The Picture of Dorian Gray', 'On its first publication The Picture of Dorian Gray was regarded as dangerously modern in its depiction of fin-de-siècle decadence. In this updated version of the Faust story, the tempter is Lord Henry Wotton, who lives selfishly for amoral pleasure; Dorian\'s good angel is the portrait painter Basil Hallward, whom Dorian murders. The book highlights the tension between the polished surface of high society and the life of secret vice. Although sin is punished in the end the book has a flavour of the elegantly perverse. With an Afterword by Peter Harness. Designed to appeal to the book lover, the Macmillan Collector\'s Library is a series of beautifully bound pocket-sized gift editions of much loved classic titles. Bound in real cloth, printed on high quality paper, and featuring ribbon markers and gilt edges, Macmillan Collector\'s Library are books to love and treasure.', 11, 'the-picture-of-dorian-gray-oscar-wilde.jpg', 'Oscar Wilde', 'Tragic descent into moral corruption as his portrait ages'),
(4, 'Animal Farm', 'All animals are equal, but some animals are more equal than others. George Orwell\'s fable of revolutionary farm animals - the steadfast horses Boxer and Clover, the opportunistic pigs Snowball and Napoleon, and the deafening choir of sheep - who overthrow their elitist human master only to find themselves subject to a new authority, is one of the most famous warnings ever written.\r\nRejected by such eminent publishing figures as Victor Gollancz, Jonathan Cape and T.S. Eliot due to its daringly open criticism of Stalin, Animal Farm was published to great acclaim by Martin Secker and Warburg on 17 August 1945. One reviewer wrote \'In a hundred years\' time perhaps Animal Farm ... may simply be a fairy story: today it is a fairy story with a good deal of point.\' Seventy-five years since its first publication, Orwell\'s immortal satire remains an unparalleled masterpiece and more relevant than ever. The authoritative text with an introduction by Christopher Hitchens.', 8, 'animal-farm-george-orwell.jpg', 'George Orwell', 'Dystopian allegory on totalitarianism, exploring risks through an animal rebellion'),
(5, 'Tales of Mystery and Imagination', 'An inescapable black pit, an innocent buried alive, and the deranged hallucinations of a murderer all haunt this collection of Edgar Allan Poe\'s most celebrated stories. The undisputed master of gothic horror, Poe probes every imaginable depth of terror in his claustrophobic nightmares of murder and madness, including the classic \'The Fall of the House of Usher\', \'The Pit and the Pendulum\' and \'The Tell-Tale Heart\'. Just as disturbing are the tales featuring the eccentric and ingenious Auguste Dupin - the first modern detective hero. In these chilling stories, Edgar Allan Poe\'s macabre imagination explores the darkest corners of the human mind and the furthest reaches of the paranormal.', 8, 'tales-of-mystery-and-imagination-edgar-allan-poe.jpg', 'Edgar Allan Poe', 'Collection of chilling and imaginative short stories'),
(8, 'Great Expectations', 'The thirteenth and penultimate novel by Charles Dickens, “Great Expectations” chronicles the education of Pip, an orphan living in mid-nineteenth century London. Including such themes as wealth and poverty, love and rejection, and triumph over evil, this novel represents a classic example of Dickensian literature not to be missed by lovers of his work. Charles John Huffam Dickens (1812–1870) was an English writer and social critic famous for having created some of the world’s most well-known fictional characters. His works became unprecedentedly popular during his life, and today he is commonly regarded as the greatest Victorian-era novelist. Although perhaps better known for such works as “Oliver Twist” or “A Christmas Carol”, Dickens first gained success with the 1836 serial publication of “The Pickwick Papers”, which turned him almost overnight into an international literary celebrity thanks to his humour, satire, and astute observations concerning society and character. This classic work is being republished now in a new edition complete with an introductory chapter from “Appreciations and Criticisms of the Works of Charles Dickens” by G. K. Chesterton.', 14, 'great-expectations-charles-dickens.jpg', 'Charles Dickens', 'Exploring themes of social class, ambition, love and rejection and triumph over evil'),
(7, 'Pet Sematary', 'A perennial classic from Stephen King - timeless and topical. Soon to be a major motion picture from Paramount. The house looked right, felt right to Dr Louis Creed. Rambling, old, unsmart and comfortable. A place where the family could settle; the children grow and play and explore. The rolling hills and meadows of Maine seemed a world away from the fume-choked dangers of Chicago.Only the occasional big truck out on the two-lane highway, grinding up through the gears, hammering down the long gradients, growled out an intrusive threat.But behind the house and far away from the road: that was safe. Just a carefully cleared path up into the woods where generations of local children have processed with the solemn innocence of the young, taking with them their dear departed pets for burial. A sad place maybe, but safe. Surely a safe place. Not a place to seep into your dreams, to wake you, sweating with fear and foreboding.', 12, 'pet-sematary-stephen-king.jpeg', 'Stepehn King', 'A chilling horror novel that delves into the consequences of tampering with death'),
(9, 'The Little Prince', 'After crash-landing in the Sahara Desert, a lonely pilot encounters a little prince who is visiting Earth from his own planet. Their strange and moving meeting illuminates many of life\'s universal truths, as he comes to learn what it means to be human from a child who is not. Antoine de Saint-Exupéry\'s delightful The Little Prince has been translated into over 180 languages and sold over 80 million copies worldwide.\r\n\r\nWith the loving, insightful, perplexed-by-grown-ups Little Prince at its heart, readers will not only rediscover characters such as The coquettish Rose, The knowledgeable Fox and The complex Lamplighter, but will find fresh and wonderful creations of these characters by a true master of his art; images that will live in our hearts and minds for generations to come.', 11, 'the-litttle-prince-antoine-de-saint-exupery.jpg', 'Antoine de Saint-Exupery', 'Learning profound lessons about life, love, and human nature'),
(10, 'The Waves', 'Set on the coast of England against the vivid background of the sea, The Waves introduces six characters—three men and three women—who are grappling with the death of a beloved friend, Percival. Instead of describing their outward expressions of grief, Virginia Woolf draws her characters from the inside, revealing them through their thoughts and interior soliloquies. As their understanding of nature’s trials grows, the chorus of narrative voices blends together in miraculous harmony, remarking not only on the inevitable death of individuals but on the eternal connection of everyone. The novel that most epitomizes Virginia Woolf’s theories of fiction in the working form, The Waves is an amazing book very much ahead of its time. It is a poetic dreamscape, visual, experimental, and thrilling.', 12, 'the-waves-virginia-woolf.jpg', 'Virginia Woolf', 'A modernist novel delving into the complexities of identity, time, and the passage of life'),
(11, 'Wuthering Heights', 'The Marjolein Bastin Classics Series is a chance to rediscover classic literature in collectible, luxuriously illustrated volumes. For the first time ever, the internationally celebrated artwork of Marjolein Bastin graces the pages of a timeless classic, Wuthering Heights, the story of Heathcliff and Catherine. Beyond bringing these stories to life, Bastin’s series adds elaborately designed ephemera, such as four-color maps, letters, family trees, and sheet music. Whether an ideal gift for a Brontë devotee or a treat to yourself, the Marjolein Bastin Classics Series, as a set or individually purchased, is perfect for anyone who feels a connection to these enduring literary gems.\r\n\r\nDiscover anew the dramatic world of Wuthering Heights. The Earnshaw family estate of Wuthering Heights lies in the rough moorland of West Yorkshire; it is here that orphan boy Heathcliff finds a permanent home with Mr. Earnshaw, his son Hindley, and his daughter Catherine. While Hindley torments his new brother, Catherine welcomes him into the family, and the two become inseparable. After Mr. Earnshaw’s death, however, Heathcliff becomes an outcast and Hindley squanders his inheritance. Catherine feels compelled to marry the wealthy neighbor, Edgar Linton, instead of her true love. Heathcliff is crushed, vowing to seek revenge on both families. Torn by his conflicting passions, Heathcliff risks everything for love, and neither time nor space—not even death—will stop him.', 15, 'wuthering-heights-emily-bronte.jpg', 'Emily Brontë', 'A Gothic novel exploring the destructive power of emotions and societal expectations.'),
(12, 'Metamorphosis', 'Introducing Little Clothbound Classics: irresistible, mini editions of short stories, novellas, and essays from the world\'s greatest writers, designed by the award-winning Coralie Bickford-Smith. Celebrating the range and diversity of Penguin Classics, they take us from snowy Japan to springtime Vienna, from haunted New England to a sun-drenched Mediterranean island, and from a game of chess on the ocean to a love story on the moon. Beautifully designed and printed, these collectible editions are bound in colourful, tactile cloth and stamped with foil. One morning, ordinary salesman Gregor Samsa wakes up to find himself transformed into a giant cockroach. Metamorphosis, Kafka\'s masterpiece of unease and black humour, is one of the twentieth century\'s most influential works of fiction and is accompanied here by two more classic stories.', 10, 'metamorphosis-franz-kafka.jpg', 'Franz Kafka', ' A surreal novella exploring themes of alienation, identity, and existentialism'),
(13, 'The Plague', 'Set in a town consumed by a deadly virus, The Plague is Albert Camus\'s world-renowned fable of fear and courage\r\n\r\nThe townspeople of Oran are in the grip of a deadly plague, which condemns its victims to a swift and horrifying death. Fear, isolation and claustrophobia follow as they are forced into quarantine. Each person responds in their own way to the lethal disease: some resign themselves to fate, some seek blame, and a few, like Dr Rieux, resist the terror.\r\n\r\nAn immediate triumph when it was published in 1947, The Plague is in part an allegory of France\'s suffering under the Nazi occupation, and a story of bravery and determination against the precariousness of human existence.', 14, 'the-plague-albert-camus.jpeg', 'Albert Camus', 'Explores existential themes, morality, and the human response to suffering and death'),
(14, 'Johny Got His Gun', 'It was the war to end all wars, the global struggle that would finally make the world safe for democracy - at any cost. But one American soldier has paid a price beyond measure. And within the disfigured flesh that was once a vision of youth lives a spirit that cannot accept what the world has become. An immediate bestseller upon its first publication in 1939, Trumbo\'s stark, profoundly troubling masterpiece about the horrors of the First World War brilliantly crystallized the uncompromising brutality of war and became the most influential protest novel of the Vietnam era, as timely as ever.', 9, 'johny-got-his-gun-dalton-trumbo.jpg', 'Dalton Trumbo', 'Reflects on the brutality of war and the dehumanizing effects of conflict');

-- --------------------------------------------------------

--
-- Štruktúra tabuľky pre tabuľku `stock`
--

DROP TABLE IF EXISTS `stock`;
CREATE TABLE IF NOT EXISTS `stock` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_id` int NOT NULL,
  `quantity` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `product_id` (`product_id`)
) ENGINE=MyISAM AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Sťahujem dáta pre tabuľku `stock`
--

INSERT INTO `stock` (`id`, `product_id`, `quantity`) VALUES
(7, 7, 8),
(6, 6, 24),
(3, 3, 11),
(4, 4, 7),
(5, 5, 11),
(8, 8, 7),
(9, 9, 3),
(10, 10, 7),
(11, 11, 14),
(12, 12, 5),
(13, 13, 13),
(15, 14, 12);

-- --------------------------------------------------------

--
-- Štruktúra tabuľky pre tabuľku `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `passwd` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `name` varchar(20) NOT NULL,
  `surname` varchar(20) NOT NULL,
  `address` varchar(50) NOT NULL,
  `discount` int NOT NULL,
  `notes` text NOT NULL,
  `role` varchar(10) DEFAULT NULL,
  `stars` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Sťahujem dáta pre tabuľku `users`
--

INSERT INTO `users` (`id`, `email`, `passwd`, `name`, `surname`, `address`, `discount`, `notes`, `role`, `stars`) VALUES
(26, 'lukas.kras@ukf.sk', '$2a$10$Q07qKEAZJGeS7V9PmohNH.AZVQet2x34TjID2JKRgYRZgM9jluj9W', 'Lukáš', 'Kras', 'Nemocničná 9, 05951 Poprad', 3, '', 'customer', 5),
(25, 'veronika.kovacova@ukf.sk', '$2a$10$gnZj3e8RT610oZ00s.Bvv.uXCqLuKmKDsDlo21ldDgGDZl/X/v6ES', 'Veronika', 'Kováčová', 'Hlavná ulica 12, 81109 Bratislava', 2, '', 'customer', 15),
(24, 'martin.novak@ukf.sk', '$2a$10$ybCM1OYKIE9whkFP4F58leMloJOF/FMpIOdpAnaskXEUZfyYWu1iu', 'Martin', 'Novák', 'Špitálska 32, 04001 Košice', 0, '', 'admin', 0),
(23, 'barbora.tomasova@ukf.sk', '$2a$10$3ICTAYXuWgeMlNcQTmDq.uHr81igiBiqzNR5kecmcu.EYqUsIG5rq', 'Barbora', 'Tomášová', 'Dolná 78, 97401 Banská Bystrica', 2, '', 'customer', 2),
(22, 'peter.svec@ukf.sk', '$2a$10$s5mwLfHGYaICGY5sdPYAkOahkHUgMfA4u8oNzezZ9NGAdT5JM9YHe', 'Peter', 'Švec', 'Mierová 234, 94901 Nitra', 0, '', 'admin', 0),
(28, 'andrej.kopan@ukf.sk', '$2a$10$N.3YvUzsdcsHg7.dFb2F2OBAdjWrGTp/wZi4JfVwccmAYM/L/rVBS', 'Andrej', 'Kopaň', 'Janská 21, 93543 Mesto', 2, '', 'customer', 0);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
