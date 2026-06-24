import { motion, useScroll, useTransform } from "framer-motion";
import { useRef } from "react";

export default function Bengaluru() {
  const ref = useRef(null);
  const { scrollYProgress } = useScroll({
    target: ref,
    offset: ["start end", "end start"]
  });

  const y = useTransform(scrollYProgress, [0, 1], ["-20%", "20%"]);

  return (
    <section ref={ref} className="relative h-screen flex items-center justify-center overflow-hidden bg-blue-50">
      <motion.div style={{ y }} className="absolute inset-0 z-0 opacity-20">
        <img 
          src="https://images.unsplash.com/photo-1524661135-423995f22d0b?q=80&w=2000&auto=format&fit=crop" 
          alt="Bengaluru Map" 
          className="w-full h-full object-cover grayscale"
        />
        <div className="absolute inset-0 bg-gradient-to-t from-blue-50 via-transparent to-blue-50"></div>
      </motion.div>

      <div className="relative z-10 text-center max-w-5xl px-margin-mobile">
        <motion.h2 
          initial={{ opacity: 0, scale: 0.9 }}
          whileInView={{ opacity: 1, scale: 1 }}
          viewport={{ once: true }}
          transition={{ duration: 1.5, ease: [0.16, 1, 0.3, 1] }}
          className="font-display-lg text-[15vw] md:text-[10vw] leading-none tracking-tighter text-transparent bg-clip-text bg-gradient-to-b from-primary-fixed-dim to-primary-fixed-dim/20"
        >
          BENGALURU.
        </motion.h2>
        <motion.p
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          transition={{ duration: 1, delay: 0.5 }}
          className="text-2xl md:text-3xl text-primary font-light mt-6 max-w-3xl mx-auto"
        >
          India's technology capital deserves smarter transit. We're tackling urban mobility challenges with high EV adoption and strong sustainability initiatives.
        </motion.p>
      </div>
    </section>
  );
}
